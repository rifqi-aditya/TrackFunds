package com.rifqi.trackfunds.ui

sealed interface MainEvent {
    data object AddActionDialogDismissed : MainEvent
    data object FloatButtonClicked : MainEvent
    data object AddTransactionManuallyClicked : MainEvent
    data object ScanReceiptClicked : MainEvent
}