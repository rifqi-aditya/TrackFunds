package com.rifqi.trackfunds.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Request Body ---
@Serializable
data class GeminiRequestDto(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String? = null,
    @SerialName("inline_data")
    val inlineData: InlineData? = null
)

@Serializable
data class InlineData(
    @SerialName("mime_type")
    val mimeType: String,
    val data: String // Ini untuk string Base64 gambar
)

// --- Response Body (disederhanakan) ---
@Serializable
data class GeminiResponseDto(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content
)

@Serializable
data class GenerationConfig(
    @SerialName("response_mime_type")
    val responseMimeType: String
)