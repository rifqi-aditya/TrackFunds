package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth

data class Budget(
    val budgetId: String,
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val budgetAmount: BigDecimal,
    val spentAmount: BigDecimal,
    val period: YearMonth
) {
    val remainingAmount: BigDecimal
        get() = budgetAmount.subtract(spentAmount)

    val progress: Float
        get() = if (budgetAmount > BigDecimal.ZERO) {
            spentAmount.divide(budgetAmount, 2, RoundingMode.HALF_UP).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }
}