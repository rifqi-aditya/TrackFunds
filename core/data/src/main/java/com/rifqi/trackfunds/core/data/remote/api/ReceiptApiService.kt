package com.rifqi.trackfunds.core.data.remote.api

import com.rifqi.trackfunds.core.data.remote.dto.ScanRequestDto
import com.rifqi.trackfunds.core.data.remote.dto.ScanResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ReceiptApiService {
    @POST("processReceipt")
    suspend fun scanReceipt(
        @Body request: ScanRequestDto
    ): ScanResponseDto
}