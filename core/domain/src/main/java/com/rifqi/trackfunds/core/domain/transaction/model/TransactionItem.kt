package com.rifqi.trackfunds.core.domain.transaction.model

import java.math.BigDecimal

data class TransactionItem(
    val id: Long = 0L,
    val name: String,
    val quantity: Int,
    val price: BigDecimal,
) {
    val total: BigDecimal
        get() = price.multiply(BigDecimal(quantity))
}