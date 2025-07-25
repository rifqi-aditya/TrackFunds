package com.rifqi.trackfunds.core.data.local.dto

import java.math.BigDecimal
import java.time.LocalDate

data class BudgetWithDetailsDto(
    val budgetId: String,
    val budgetAmount: BigDecimal?,
    val period: LocalDate,

    val categoryId: String,
    val categoryName: String?,
    val categoryIconIdentifier: String?,

    val spentAmount: BigDecimal
)