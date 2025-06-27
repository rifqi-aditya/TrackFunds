package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.remote.api.ReceiptApiService
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(
    private val apiService: ReceiptApiService,
    @ApplicationContext private val context: Context
) : ScanRepository {

    override suspend fun scanReceipt(imageUri: Uri): ScanResult {
        // FIX: Ubah Uri menjadi MultipartBody.Part
        val imagePart = imageUri.toMultipartBodyPart(context)

        // Panggil API dengan file part tersebut
        val responseDto = apiService.scanReceipt(imagePart)

        return responseDto.toDomain()
    }
}

// Fungsi helper untuk mengubah Uri menjadi MultipartBody.Part
private fun Uri.toMultipartBodyPart(context: Context): MultipartBody.Part {
    // 1. Dapatkan stream data dari Uri
    val inputStream = context.contentResolver.openInputStream(this)
    // 2. Baca semua byte dari gambar
    val fileBytes = inputStream?.readBytes()
    inputStream?.close()

    // 3. Buat RequestBody dari byte array gambar
    // "image/jpeg" adalah tipe MIME, sesuaikan jika Anda menggunakan PNG
    val requestFile = fileBytes?.toRequestBody("image/jpeg".toMediaTypeOrNull())

    // 4. Buat MultipartBody.Part
    // "image" adalah nama field yang akan diterima backend
    // "receipt.jpg" adalah nama file yang akan diterima backend
    return MultipartBody.Part.createFormData("data", "receipt.jpg", requestFile!!)
}