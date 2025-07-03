package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

data class BudgetItem(
    val budgetId: String,
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val budgetAmount: BigDecimal, // Jumlah yang dianggarkan
    val spentAmount: BigDecimal,  // Jumlah yang sudah dihabiskan
    val period: String // Contoh: "2025-06" untuk Juni 2025
) {
    // Properti bantuan untuk kemudahan di UI
    val remainingAmount: BigDecimal
        get() = (budgetAmount ?: BigDecimal.ZERO).subtract(spentAmount)

    val progress: Float
        get() = if (budgetAmount > BigDecimal.ZERO) {
            spentAmount.divide(budgetAmount, 2, RoundingMode.HALF_UP).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }
}