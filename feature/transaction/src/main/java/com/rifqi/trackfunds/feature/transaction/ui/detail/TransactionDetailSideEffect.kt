package com.rifqi.trackfunds.feature.transaction.ui.detail

sealed interface TransactionDetailSideEffect {
    data class NavigateToEdit(val transactionId: String) : TransactionDetailSideEffect
    data object NavigateBackAfterDelete : TransactionDetailSideEffect
    data class ShowSnackbar(val message: String) : TransactionDetailSideEffect
    data class ViewReceipt(val imageUriString: String) : TransactionDetailSideEffect
}