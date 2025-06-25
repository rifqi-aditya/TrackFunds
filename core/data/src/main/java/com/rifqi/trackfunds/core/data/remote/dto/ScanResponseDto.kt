package com.rifqi.trackfunds.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScanResponseDto(
    @SerialName("merchantName")
    val detectedMerchant: String?,

    @SerialName("totalAmount")
    val totalAmount: Double?, // Menggunakan Double agar lebih mudah diproses

    @SerialName("transactionDateTime")
    val transactionDate: String?, // Format YYYY-MM-DD

    @SerialName("suggestedCategoryKey")
    val suggestedCategoryKey: String?
)