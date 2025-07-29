package com.rifqi.trackfunds.feature.savings.ui.detail

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.TransactionModel

data class SavingsDetailUiState(
    val isLoading: Boolean = true,
    val goal: SavingsGoal? = null,
    val history: List<TransactionModel> = emptyList(),
    val error: String? = null,

    val showDeleteConfirmDialog: Boolean = false
)