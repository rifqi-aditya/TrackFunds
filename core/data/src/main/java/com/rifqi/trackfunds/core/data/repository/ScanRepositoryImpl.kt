package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.UnknownException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.dto.GeminiResponseDto
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.common.exception.ScanException
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult
import com.rifqi.trackfunds.core.domain.scan.repository.ScanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val categoryDao: CategoryDao,
    @ApplicationContext private val context: Context
) : ScanRepository {

    private var cachedCategories: List<CategoryEntity>? = null

    override suspend fun extractTextFromImage(imageUri: Uri): Result<String> {
        return runCatching {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromFilePath(context, imageUri)
            val visionText = recognizer.process(image).await()
            visionText.text.ifBlank {
                throw ScanException.NoTextFound()
            }
        }
    }

    override suspend fun analyzeReceiptText(ocrText: String, userUid: String): Result<ScanResult> {
        return try {
            // --- Bagian ini tetap sama ---
            val categories = cachedCategories ?: categoryDao.getFilteredCategories(
                userUid = userUid,
                type = null,
                isUnbudgeted = null,
                budgetPeriod = null
            ).first().also {
                cachedCategories = it
            }
            val prompt = createGeminiPrompt(ocrText, categories)
            val response = generativeModel.generateContent(prompt)
            val responseText = response.text ?: throw ScanException.ParsingFailed()

            val cleanedJson = responseText.removePrefix("```json").removeSuffix("```").trim()
            val json = Json { ignoreUnknownKeys = true }
            val dto = json.decodeFromString<GeminiResponseDto>(cleanedJson)

            if (dto.totalAmount == null && dto.merchantName == null) {
                return Result.failure(ScanException.NotAReceipt())
            }

            // 3. Jika valid, baru map ke ScanResult
            val scanResult = mapResponse(dto)
            Result.success(scanResult)

        } catch (e: SerializationException) {
            Result.failure(ScanException.ParsingFailed())
        } catch (e: UnknownException) {
            Result.failure(ScanException.NetworkError())
        } catch (e: Exception) {
            Result.failure(ScanException.UnknownError(e.message))
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
            1.  **PRIMARY GOAL:** First, determine if the provided OCR text is from a sales receipt. If the text is clearly not from a receipt (e.g., a random note, a menu, a poster), you MUST return a JSON object where key fields like `merchant_name` and `total_amount` are `null`.
            2.  **EXTRACTION:** If it IS a receipt, extract `merchant_name`, `transaction_date`, `transaction_time`, `total_amount`, and `line_items`.
            3.  **MISSING DATA:** If a specific field's value cannot be found on the receipt, use `null` for that field in the JSON. **DO NOT INVENT or guess data.** For `total_amount`, find the most likely final total, often labeled "Total" or being the largest amount.
            4.  **FORMATTING:** Standardize `transaction_date` to `YYYY-MM-DD` format (use today's date, $currentDate, if it's missing). Standardize `transaction_time` to `HH:mm:ss` (use `null` if missing). `total_amount` must be a single number (Double or Integer), without currency symbols or commas.
            5.  **LINE ITEMS:** `line_items` must be an array of objects, each with `name` (String), `quantity` (Number, default 1), and `price` (Number). If no individual items are clearly listed, you MUST use an empty array `[]`.
            6.  **CATEGORY:** Analyze the merchant and items to select the single most relevant `category_key` from the list provided. If no category is a good match, you MUST use "miscellaneous".
            7.  **OUTPUT:** Respond with ONLY the raw JSON object. Do not include any explanatory text, greetings, or markdown formatting like ```json.

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
     * Fungsi ini sekarang hanya bertanggung jawab untuk MAPPING dari DTO ke Domain,
     * bukan lagi parsing.
     */
    private fun mapResponse(dto: GeminiResponseDto): ScanResult {
        val date = dto.transactionDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
        val time = dto.transactionTime?.let { LocalTime.parse(it) } ?: LocalTime.MIDNIGHT
        val transactionDateTime = LocalDateTime.of(date, time)

        return ScanResult(
            merchantName = dto.merchantName,
            transactionDateTime = transactionDateTime,
            totalAmount = BigDecimal(dto.totalAmount?.toString() ?: "0"),
            categoryStandardKey = dto.category,
            transactionItem = dto.items.map { it.toDomain() }
        )
    }
}