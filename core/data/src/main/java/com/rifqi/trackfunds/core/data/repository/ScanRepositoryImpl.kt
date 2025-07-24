package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.remote.api.GeminiApiService
import com.rifqi.trackfunds.core.data.remote.dto.Content
import com.rifqi.trackfunds.core.data.remote.dto.GeminiRequestDto
import com.rifqi.trackfunds.core.data.remote.dto.GenerationConfig
import com.rifqi.trackfunds.core.data.remote.dto.InlineData
import com.rifqi.trackfunds.core.data.remote.dto.Part
import com.rifqi.trackfunds.core.data.remote.dto.ScanResponseDto
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService,
    private val categoryDao: CategoryDao,
    @ApplicationContext private val context: Context
) : ScanRepository {

    override suspend fun scanReceipt(imageUri: Uri): ScanResult {
        val imageBase64 = imageUri.toBase64(context)
            ?: throw Exception("Gagal mengonversi gambar ke Base64.")

        val categories = categoryDao.getFilteredCategories(
            type = null,
            userUid = "",
            isUnbudgeted = null,
            budgetPeriod = null
        ).first()

        val prompt = createGeminiPrompt(categories)

        val request = GeminiRequestDto(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(mimeType = "image/jpeg", data = imageBase64))
                    )
                )
            ),
            generationConfig = GenerationConfig(responseMimeType = "application/json")
        )

        val geminiResponse = geminiApiService.generateContent(
            apiKey = "AIzaSyD1ANUOVPW7eTch2Lj_-x_FgN1KShJiHhc",
            request = request
        )

        val jsonText = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Respons dari Gemini kosong atau tidak valid.")

        val scanResponseDto = Json.decodeFromString<ScanResponseDto>(jsonText)

        return scanResponseDto.toDomain()
    }
}

private fun Uri.toBase64(context: Context): String? {
    return context.contentResolver.openInputStream(this)?.use {
        Base64.encodeToString(it.readBytes(), Base64.NO_WRAP)
    }
}

private fun createGeminiPrompt(categories: List<CategoryEntity>): String {
    val categoryListString = categories.joinToString("\n") { "- ${it.standardKey}: ${it.name}" }

    val promptTemplate = """
        Anda adalah asisten keuangan ahli dengan tugas menganalisis struk dari sebuah gambar untuk aplikasi pencatatan keuangan.

        Tugas Anda:
        Ekstrak informasi dari gambar, lakukan standardisasi, tentukan kategori yang paling sesuai dari daftar yang saya berikan, dan kembalikan output HANYA dalam format objek JSON yang valid.

        IKUTI ATURAN INI DENGAN KETAT:

        1.  **Ekstrak Informasi:**
            * `merchant_name`: Nama toko atau penjual. Jika tidak ada, gunakan `null`.
            * `transaction_date`: Tanggal transaksi.
            * `transaction_time`: Waktu transaksi.
            * `total_amount`: Jumlah total pembayaran.

        2.  **Standardisasi:**
            * Untuk `transaction_date`, ubah ke format **YYYY-MM-DD**. Jika tidak ada, gunakan tanggal hari ini: [TANGGAL HARI INI].
            * Untuk `transaction_time`, ubah ke format **HH:MM:SS**. Jika tidak ada, gunakan `null`.
            * Untuk `total_amount`, ubah menjadi **angka (integer)** tanpa titik, koma, atau simbol mata uang. Jika tidak ada, gunakan `0`.

        3.  **Penentuan Kategori (Paling Penting):**
            * Analisis `merchant_name` dan item-item pada struk.
            * Pilih **SATU** `key` kategori yang paling relevan dari daftar di bawah ini. Jangan menebak kategori lain. Jika tidak ada yang cocok, gunakan `miscellaneous`.

        4.  **Format Output:**
            * Berikan output **HANYA** dalam format objek JSON. Jangan tambahkan ```json```, penjelasan, atau teks lain di luar objek JSON.
            * Jika struk tidak dapat dibaca sama sekali, berikan output ini: `{"error": true, "message": "Struk tidak dapat dibaca"}`

        ---
        **DAFTAR KATEGORI YANG TERSEDIA SAAT INI:**
        [DAFTAR_KATEGORI]
        ---
    """.trimIndent()

    return promptTemplate.replace("[DAFTAR_KATEGORI]", categoryListString)
}