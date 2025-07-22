package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionItem

data class TransactionDetailUiState(
    val isLoading: Boolean = true,
    val transaction: TransactionItem? = null,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false
)