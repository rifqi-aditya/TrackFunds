package com.rifqi.trackfunds.core.data.remote.api

import com.rifqi.trackfunds.core.data.remote.dto.GeminiRequestDto
import com.rifqi.trackfunds.core.data.remote.dto.GeminiResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): GeminiResponseDto
}