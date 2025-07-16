package com.rifqi.trackfunds.event

sealed interface TrackFundsMainEvent {
    data object AddActionDialogDismissed : TrackFundsMainEvent
    data object FloatButtonClicked : TrackFundsMainEvent
    data object AddTransactionManuallyClicked : TrackFundsMainEvent
    data object ScanReceiptClicked : TrackFundsMainEvent
}