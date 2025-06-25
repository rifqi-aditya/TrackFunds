package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.remote.api.ReceiptApiService
import com.rifqi.trackfunds.core.data.remote.dto.ScanRequestDto
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(
    private val apiService: ReceiptApiService,
    @ApplicationContext private val context: Context
) : ScanRepository {

    override suspend fun scanReceipt(imageUri: Uri): ScanResult {
        // 1. Konversi gambar dari Uri ke String Base64
        val imageBase64 = imageUri.toBase64String(context)

        // 2. Buat objek Request DTO
        val requestDto = ScanRequestDto(image = imageBase64)

        // 3. Panggil API dengan DTO tersebut
        val responseDto = apiService.scanReceipt(requestDto)

        // 4. Map DTO respons ke Domain Model (tidak ada perubahan di sini)
        return responseDto.toDomain()
    }
}

// Fungsi helper untuk mengubah Uri menjadi Base64 String
private fun Uri.toBase64String(context: Context): String {
    val inputStream = context.contentResolver.openInputStream(this)
    val bytes = inputStream?.readBytes()
    inputStream?.close()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}