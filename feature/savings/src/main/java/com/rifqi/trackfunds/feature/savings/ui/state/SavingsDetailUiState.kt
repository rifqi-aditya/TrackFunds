package com.rifqi.trackfunds.feature.savings.ui.state

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel

data class SavingsDetailUiState(
    val isLoading: Boolean = true,
    val goal: SavingsGoalModel? = null,
    val history: List<TransactionModel> = emptyList(),
    val error: String? = null,

    val showDeleteConfirmDialog: Boolean = false
)