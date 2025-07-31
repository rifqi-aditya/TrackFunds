package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class LineItem(
    val name: String,
    val quantity: Int,
    val price: BigDecimal
)