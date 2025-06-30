package com.rifqi.trackfunds.feature.transaction.ui.event

sealed interface TransactionDetailEvent {
    data object EditClicked : TransactionDetailEvent
    data object DeleteClicked : TransactionDetailEvent
    data object ConfirmDeleteClicked : TransactionDetailEvent
    data object DismissDeleteDialog : TransactionDetailEvent
}