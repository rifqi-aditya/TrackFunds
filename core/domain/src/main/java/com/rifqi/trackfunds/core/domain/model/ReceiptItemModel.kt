package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class ReceiptItemModel(
    val name: String,
    val quantity: Int,
    val price: BigDecimal
)