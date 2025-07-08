package com.rifqi.trackfunds.feature.savings.ui.state

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem

data class SavingsDetailUiState(
    val isLoading: Boolean = true,
    val goal: SavingsGoalItem? = null,
    val history: List<TransactionItem> = emptyList(),
    val error: String? = null,

    val showDeleteConfirmDialog: Boolean = false
)