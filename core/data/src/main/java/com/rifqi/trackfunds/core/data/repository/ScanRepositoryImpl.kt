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
        Log.d("ScanRepository", "Extracted text: $raw")
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
            Log.d("ScanRepository", "Gemini response: $responseText")

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
            val invalidTotal = (dto.totalAmount == null) || (dto.totalAmount <= 1000.0) // cegah "9"

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
    private fun createGeminiPrompt(ocrText: String, categories: List<CategoryEntity>): String {
        val categoryListString = categories.joinToString("\n") { "- ${it.standardKey}: ${it.name}" }
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        return """
      You analyze OCR text of a retail receipt. Respond with ONLY a valid JSON object (no markdown).

      Decide if the text is a sales receipt and extract fields. If unsure, be conservative.

      JSON schema (unknown fields allowed):
      {
        "is_receipt": boolean,
        "quality_score": number,            // 0.0 .. 1.0 confidence
        "merchant_name": string|null,
        "transaction_date": "YYYY-MM-DD"|null,   // if missing, use "$today"
        "transaction_time": "HH:mm:ss"|null,
        "total_amount": number|null,             // no currency symbols, dot as decimal
        "items": [ { "name": string, "quantity": number|null, "price": number|null }, ... ],
        "category": string                        // one of provided keys, else "miscellaneous"
      }

      Rules:
        - If NOT a receipt: set "is_receipt": false and "quality_score": 0.0; keep other fields null/empty.
        - Do NOT guess: if a value is unknown or not explicitly supported by the text, return null.
        - "total_amount" = final payable total.
        - Items: output lines that look like items; quantity/price may be null if unclear.
        - Merchant name (very important):
          • Fill ONLY if a clear brand/store name is explicitly present in the text.
          • Do NOT infer from addresses, building names, tax IDs, phone/emails, URLs, or generic words (e.g., street/city/“minimarket”).
          • If uncertain, set merchant_name = null and reduce quality_score (≤ 0.6).
        - Normalize Indonesian numerals: "124,000"→124000 ; "11,273"→11273.

      AVAILABLE CATEGORIES:
      $categoryListString

      OCR TEXT:
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
            merchantName = dto.merchantName?.lowercase()?.trim(),
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