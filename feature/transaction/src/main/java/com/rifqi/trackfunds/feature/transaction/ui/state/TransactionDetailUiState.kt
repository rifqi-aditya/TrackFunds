package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionModel

data class TransactionDetailUiState(
    val isLoading: Boolean = true,
    val transaction: TransactionModel? = null,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false
)