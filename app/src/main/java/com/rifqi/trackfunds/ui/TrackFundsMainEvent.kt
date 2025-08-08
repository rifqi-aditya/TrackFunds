package com.rifqi.trackfunds.ui

sealed interface TrackFundsMainEvent {
    data object AddActionDialogDismissed : TrackFundsMainEvent
    data object FloatButtonClicked : TrackFundsMainEvent
    data object AddTransactionManuallyClicked : TrackFundsMainEvent
    data object ScanReceiptClicked : TrackFundsMainEvent
}