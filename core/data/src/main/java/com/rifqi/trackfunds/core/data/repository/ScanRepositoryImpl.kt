package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
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

    override suspend fun extractTextFromImage(imageUri: Uri): Result<String> = runCatching {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromFilePath(context, imageUri)
        val vt = recognizer.process(image).await()
        val raw = vt.text.trim()
        if (raw.isBlank()) throw ScanException.NoTextFound()
        if (raw.length < 20) throw ScanException.NoTextFound()
        raw
    }

    override suspend fun analyzeReceiptText(ocrText: String, userUid: String): Result<ScanResult> {
        return try {
            // 1) Ambil kategori (cache)
            val categories = cachedCategories ?: categoryDao.getFilteredCategories(
                userUid = userUid,
                type = null,
                isUnbudgeted = null,
                budgetPeriod = null
            ).first().also { cachedCategories = it }

            // 2) Prompt ke Gemini
            val prompt = createGeminiPrompt(ocrText, categories)
            val response = generativeModel.generateContent(prompt)
            val responseText = response.text ?: throw ScanException.ParsingFailed()

            // 3) Sanitasi & parse JSON
            val cleanedJson = sanitizeJsonBlock(responseText)
            Log.d("ScanRepository", "Cleaned JSON: $cleanedJson")
            val json = Json { ignoreUnknownKeys = true }
            val dto = json.decodeFromString<GeminiResponseDto>(cleanedJson)
            Log.d("ScanRepository", "Parsed DTO: $dto")

            if (!dto.isReceipt) {
                return Result.failure(ScanException.NotAReceipt())
            }

            val quality = dto.qualityScore ?: 0.0
            val tooLowQuality = quality < 0.6
            val invalidTotal = (dto.totalAmount == null) || (dto.totalAmount <= 1000.0)

            if (tooLowQuality || invalidTotal) {
                return Result.failure(ScanException.LowConfidence("low quality or total missing/small"))
            }

            // 5) Map -> Domain (hanya setelah valid)
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
    private fun createGeminiPrompt(
        ocrText: String,
        categories: List<CategoryEntity>
    ): String {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val categoryKeys = categories.joinToString(", ") { "\"${it.standardKey}\"" }
        Log.d("ScanRepository", "Available categories: $categoryKeys")

        return """
            You analyze OCR text of a retail receipt from indonesia.
            
            Return ONLY a valid JSON object — no explanations, no markdown, no code fences, no extra text.
               
            Currency & locale:
            - Assume Indonesian format: "." thousands, "," decimals.
            - Output monetary amounts in **integer Rupiah** (no decimals). Examples:
              "Rp 75.000" -> 75000, "12.345,67" -> 12346
  
            Rules:
            - If a field is unknown, use null.
            - Use Indonesian locale for currency. Output monetary amounts in integer Rupiah (no decimals).
            - Dates must be "YYYY-MM-DD". If missing, use "$today".
            - Time must be "HH:mm" or null.
            - "items" should contain only actual line items (name, optional quantity, optional price).
            - "category" must be one of: [$categoryKeys]. If none fits, use "miscellaneous".
            - Be conservative: set is_receipt = false if the text is not clearly a receipt.
            
            Short description (IMPORTANT):
            - Provide a concise Indonesian description (max 8–10 word) summarizing the purchase,
            - Do **not** guess or invent store/brand names. If unclear, write a generic description.
            - Never include address, NPWP, or transaction number.
            
            Schema:
            {
              "is_receipt": boolean,
              "quality_score": number,
              "description": string|null,
              "transaction_date": "YYYY-MM-DD"|null,
              "transaction_time": "HH:mm"|null,
              "total_amount": number|null,
              "items": [ { "name": string, "quantity": number|null, "price": number|null } ],
              "category": string
            }
            
            OCR_TEXT:
            $ocrText
            """.trimIndent()
    }


    /**
     * Fungsi ini sekarang hanya bertanggung jawab untuk MAPPING dari DTO ke Domain,
     * bukan lagi parsing.
     */
    private fun mapResponse(dto: GeminiResponseDto): ScanResult {
        val date = dto.transactionDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
        val time = dto.transactionTime?.let { LocalTime.parse(it) } ?: LocalTime.MIDNIGHT
        val dateTime = LocalDateTime.of(date, time)

        return ScanResult(
            description = dto.description,
            transactionDateTime = dateTime,
            totalAmount = BigDecimal(dto.totalAmount?.toString() ?: "0"),
            categoryStandardKey = dto.category,
            transactionItem = dto.items.map { it.toDomain() }
        )
    }


    private fun sanitizeJsonBlock(text: String): String {
        // Ambil blok JSON di dalam balasan Gemini (tanpa ```json ... ```)
        val fenced = Regex("```(?:json)?\\s*([\\s\\S]*?)\\s*```", RegexOption.IGNORE_CASE)
        val m = fenced.find(text)
        return (m?.groupValues?.getOrNull(1) ?: text).trim()
    }
}