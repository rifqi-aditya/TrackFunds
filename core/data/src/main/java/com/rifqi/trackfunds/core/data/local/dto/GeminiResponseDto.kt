package com.rifqi.trackfunds.core.data.local.dto

import com.rifqi.trackfunds.core.domain.transaction.model.TransactionItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class GeminiResponseDto(
    @SerialName("is_receipt")
    val isReceipt: Boolean = false,

    @SerialName("quality_score")
    val qualityScore: Double? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("total_amount")
    val totalAmount: Double? = null,

    @SerialName("transaction_date")
    val transactionDate: String? = null,

    @SerialName("transaction_time")
    val transactionTime: String? = null,

    @SerialName("category")
    val category: String? = null,

    @SerialName("items")
    val items: List<ItemDto> = emptyList()
)

@Serializable
data class ItemDto(
    @SerialName("name") val name: String? = null,
    @SerialName("quantity") val quantity: Double? = 1.0,
    @SerialName("price") val price: Double? = null
) {
    fun toDomain(): TransactionItem {
        return TransactionItem(
            name = name.orEmpty(),
            quantity = (quantity ?: 1.0).toInt().coerceAtLeast(1),
            price = BigDecimal((price ?: 0.0).toString())
        )
    }
}
