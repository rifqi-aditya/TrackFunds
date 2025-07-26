package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import com.google.ai.client.generativeai.GenerativeModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Serializable
private data class GeminiResponseDto(
    @SerialName("merchant_name")
    val merchantName: String? = null,

    @SerialName("total_amount")
    val totalAmount: Double? = null, // Menggunakan Double atau Long/Int

    @SerialName("transaction_date")
    val transactionDate: String? = null, // Format YYYY-MM-DD

    @SerialName("transaction_time")
    val transactionTime: String? = null, // Format HH:mm:ss

    @SerialName("category_key")
    val category: String? = null, // Ini akan berisi "standardKey"

    @SerialName("line_items")
    val items: List<ReceiptItemDto> = emptyList()
)

@Serializable
private data class ReceiptItemDto(
    val name: String,
    val quantity: Int = 1,
    val price: Double
) {
    fun toDomain(): ReceiptItemModel {
        return ReceiptItemModel(
            name = name,
            quantity = quantity,
            price = BigDecimal(price.toString())
        )
    }
}

@Singleton
class ScanRepositoryImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val generativeModel: GenerativeModel,
    private val categoryDao: CategoryDao,
    @ApplicationContext private val context: Context
) : ScanRepository {

    override suspend fun extractTextFromImage(imageUri: Uri): Result<String> {
        return try {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromFilePath(context, imageUri)
            val visionText = recognizer.process(image).await()
            if (visionText.text.isBlank()) {
                Result.failure(Exception("No text could be read from the image."))
            } else {
                Result.success(visionText.text)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun analyzeReceiptText(ocrText: String): Result<ScanResult> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            val categories = categoryDao.getFilteredCategories(
                userUid = userUid,
                type = null,
                isUnbudgeted = null,
                budgetPeriod = null
            ).first()

            val prompt = createGeminiPrompt(ocrText, categories)
            val response = generativeModel.generateContent(prompt)
            val responseText = response.text
                ?: return Result.failure(Exception("Gemini returned an empty response."))

            val scanResult = parseAndMapResponse(responseText)

            println(scanResult)

            Result.success(scanResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Membuat prompt yang akan dikirim ke Gemini.
     */
    private fun createGeminiPrompt(ocrText: String, categories: List<CategoryEntity>): String {
        val categoryListString = categories.joinToString("\n") { "- ${it.standardKey}: ${it.name}" }
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        return """
            You are an expert financial assistant analyzing OCR text from a receipt.
            Your task is to extract information, standardize it, determine the most suitable category from the provided list, and return the output ONLY in a valid JSON object format.

            RULES:
            1.  Extract: `merchant_name`, `transaction_date`, `transaction_time`, `total_amount`, and `line_items`.
            2.  `line_items` must be an array of objects, each with `name` (String), `quantity` (Number, default to 1), and `price` (Number). Use an empty array `[]` if no items are detected.
            3.  Standardize `transaction_date` to YYYY-MM-DD (use $currentDate if missing) and `transaction_time` to HH:MM:SS (use null if missing). Standardize amounts to integers.
            4.  Analyze the text and select ONE `key` from the category list below that is most relevant. Put it in the `category_key` field. If nothing matches, use `miscellaneous`.
            5.  Output ONLY the JSON object.

            ---
            AVAILABLE CATEGORIES:
            $categoryListString
            ---
            OCR TEXT TO ANALYZE:
            $ocrText
            ---
        """.trimIndent()
    }

    /**
     * Mem-parsing JSON dari Gemini dan memetakannya ke model domain ScanResult.
     */
    private fun parseAndMapResponse(jsonString: String): ScanResult {
        val cleanedJson = jsonString.removePrefix("```json").removeSuffix("```").trim()
        val json = Json { ignoreUnknownKeys = true }
        val dto = json.decodeFromString<GeminiResponseDto>(cleanedJson)

        // 1. Gabungkan tanggal dan waktu menjadi LocalDateTime
        val date = dto.transactionDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
        val time = dto.transactionTime?.let { LocalTime.parse(it) } ?: LocalTime.MIDNIGHT
        val transactionDateTime = LocalDateTime.of(date, time)

        // 2. Petakan semua data ke ScanResult
        return ScanResult(
            merchantName = dto.merchantName,
            transactionDateTime = transactionDateTime,
            totalAmount = BigDecimal(dto.totalAmount?.toString() ?: "0"),
            categoryStandardKey = dto.category,
            receiptItemModels = dto.items.map { it.toDomain() }
        )
    }
}