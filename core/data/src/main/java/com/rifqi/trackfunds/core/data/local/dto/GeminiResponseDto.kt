package com.rifqi.trackfunds.core.data.local.dto

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class GeminiResponseDto(
    @SerialName("merchant_name")
    val merchantName: String? = null,

    @SerialName("total_amount")
    val totalAmount: Double? = null, // Menggunakan Double atau Long/Int

    @SerialName("transaction_date")
    val transactionDate: String? = null, // Format YYYY-MM-DD

    @SerialName("transaction_time")
    val transactionTime: String? = null, // Format HH:mm:ss

    @SerialName("category_key")
    val category: String? = null, // Ini akan berisi "standardKey"

    @SerialName("line_items")
    val items: List<ReceiptItemDto> = emptyList()
)

@Serializable
data class ReceiptItemDto(
    val name: String,
    val quantity: Int = 1,
    val price: Double
) {
    fun toDomain(): TransactionItem {
        return TransactionItem(
            name = name,
            quantity = quantity,
            price = BigDecimal(price.toString())
        )
    }
}