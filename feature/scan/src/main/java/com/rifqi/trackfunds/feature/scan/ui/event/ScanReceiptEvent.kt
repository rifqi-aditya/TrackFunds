package com.rifqi.trackfunds.feature.scan.ui.event

import android.net.Uri

sealed interface ScanReceiptEvent {
    // Fase UPLOAD
    data object SelectFromGalleryClicked : ScanReceiptEvent
    data object SelectFromCameraClicked : ScanReceiptEvent
    data class ImageSelected(val uri: Uri) : ScanReceiptEvent
    data object ConfirmImage : ScanReceiptEvent

    // Aksi Umum
    data object ScanReceiptAgainClicked : ScanReceiptEvent
}