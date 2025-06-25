package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class ScanResult(
    val amount: BigDecimal?,
    val date: LocalDate?,
    val note: String?,
    val suggestedCategoryKey: String?
)