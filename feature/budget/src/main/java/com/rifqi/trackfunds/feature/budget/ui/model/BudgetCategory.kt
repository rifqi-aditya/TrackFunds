package com.rifqi.trackfunds.feature.budget.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BudgetCategory(
    val name: String,
    val icon: ImageVector, // Using ikon from Material Icons
    val budgetedAmount: Float,
    val spentAmount: Float
) {
    val remainingAmount: Float
        get() = budgetedAmount - spentAmount
    
    val progress: Float
        get() = if (budgetedAmount > 0f) {
            (spentAmount / budgetedAmount).coerceIn(0f, 1f)
        } else {
            0f
        }
}