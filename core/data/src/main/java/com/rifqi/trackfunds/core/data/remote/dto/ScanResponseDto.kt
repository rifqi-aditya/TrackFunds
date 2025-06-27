package com.rifqi.trackfunds.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScanResponseDto(
    @SerialName("merchant_name")
    val merchantName: String? = null,

    @SerialName("total_amount")
    val totalAmount: Double? = null, // Menggunakan Double atau Long/Int

    @SerialName("transaction_date")
    val transactionDate: String? = null, // Format YYYY-MM-DD

    @SerialName("transaction_time")
    val transactionTime: String? = null, // Format HH:mm:ss

    @SerialName("category")
    val category: String? = null // Ini akan berisi "standardKey"
)