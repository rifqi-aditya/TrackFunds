package com.rifqi.trackfunds.feature.transaction.ui.detail

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction

data class TransactionDetailUiState(
    val isLoading: Boolean = true,
    val transaction: Transaction? = null,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false
)