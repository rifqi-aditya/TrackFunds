package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class ScanResult(
    val amount: BigDecimal?,
    val date: LocalDateTime?,
    val description: String?,
    val suggestedCategoryKey: String?
)