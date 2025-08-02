package com.rifqi.trackfunds.feature.savings.ui.detail

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction

data class SavingsDetailUiState(
    val isLoading: Boolean = true,
    val goal: SavingsGoal? = null,
    val history: List<Transaction> = emptyList(),
    val error: String? = null,

    val showDeleteConfirmDialog: Boolean = false
)