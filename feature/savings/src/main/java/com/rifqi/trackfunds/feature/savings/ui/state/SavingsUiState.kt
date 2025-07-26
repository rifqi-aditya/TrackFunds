package com.rifqi.trackfunds.feature.savings.ui.state

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import java.math.BigDecimal

/**
 * Represents all data needed for the Savings list screen.
 */
data class SavingsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // Total dari semua saldo tabungan yang aktif
    val totalSavings: BigDecimal = BigDecimal.ZERO,

    // Daftar semua tujuan tabungan yang aktif
    val savingsGoalModels: List<SavingsGoalModel> = emptyList()
)