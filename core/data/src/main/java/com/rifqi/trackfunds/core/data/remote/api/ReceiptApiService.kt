package com.rifqi.trackfunds.core.data.remote.api

import com.rifqi.trackfunds.core.data.remote.dto.ScanResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReceiptApiService {

    @Multipart
    @POST("1e4a416f-4852-47fd-ad20-5a7d94f347cb")
    suspend fun scanReceipt(
        @Part data: MultipartBody.Part
    ): ScanResponseDto
}