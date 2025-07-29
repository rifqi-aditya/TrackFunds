package com.rifqi.trackfunds.feature.savings.ui.goals_list

import com.rifqi.trackfunds.feature.savings.ui.model.SavingsGoalUiModel
import java.math.BigDecimal

/**
 * Represents all data needed for the Savings list screen.
 */
data class SavingsGoalUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // Total dari semua saldo tabungan yang aktif
    val totalSaved: String = "Rp 0",
    val totalTarget: BigDecimal = BigDecimal.ZERO,
    val totalRemaining: BigDecimal = BigDecimal.ZERO,

    // Daftar semua tujuan tabungan yang aktif
    val savingsGoals: List<SavingsGoalUiModel> = emptyList()
)