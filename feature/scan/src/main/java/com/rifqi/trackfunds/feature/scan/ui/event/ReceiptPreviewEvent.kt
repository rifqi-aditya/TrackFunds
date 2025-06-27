package com.rifqi.trackfunds.feature.scan.ui.event

sealed interface ReceiptPreviewEvent {
    data object ConfirmScanClicked : ReceiptPreviewEvent
}