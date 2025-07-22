package com.rifqi.trackfunds.feature.savings.ui.state

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.Transaction

data class SavingsDetailUiState(
    val isLoading: Boolean = true,
    val goal: SavingsGoalItem? = null,
    val history: List<Transaction> = emptyList(),
    val error: String? = null,

    val showDeleteConfirmDialog: Boolean = false
)