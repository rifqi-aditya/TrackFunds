package com.rifqi.trackfunds.feature.scan.ui.sideeffect

sealed interface ScanReceiptSideEffect {
    // Perintah Navigasi
    data object NavigateToCamera : ScanReceiptSideEffect
    data object LaunchGallery : ScanReceiptSideEffect
    data object NavigateBack : ScanReceiptSideEffect
    data object NavigateToAddTransaction : ScanReceiptSideEffect
}