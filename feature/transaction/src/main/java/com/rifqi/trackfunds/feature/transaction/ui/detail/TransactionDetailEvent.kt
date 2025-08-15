package com.rifqi.trackfunds.feature.transaction.ui.detail

sealed interface TransactionDetailEvent {
    data object EditClicked : TransactionDetailEvent
    data object DeleteClicked : TransactionDetailEvent
    data object ConfirmDeleteClicked : TransactionDetailEvent
    data object DismissDeleteDialog : TransactionDetailEvent
    data class ReceiptClicked(val imageUriString: String) : TransactionDetailEvent
}