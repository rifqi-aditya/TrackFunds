package com.rifqi.trackfunds.feature.scan.ui.sideeffect

sealed interface ScanReceiptSideEffect {
    // Perintah Navigasi
    data object NavigateToCamera : ScanReceiptSideEffect
    data object LaunchGallery : ScanReceiptSideEffect
//    data object NavigateToSelectAccount : ScanReceiptSideEffect
//    data object NavigateToSelectCategory : ScanReceiptSideEffect
    data object NavigateBack : ScanReceiptSideEffect

    // Perintah UI Lainnya
//    data class ShowSnackbar(val message: String) : ScanReceiptSideEffect
}