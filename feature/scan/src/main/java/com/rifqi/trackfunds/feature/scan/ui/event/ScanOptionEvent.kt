package com.rifqi.trackfunds.feature.scan.ui.event

import android.net.Uri

sealed interface ScanOptionEvent {
    data object SelectFromGalleryClicked : ScanOptionEvent
    data object SelectFromCameraClicked : ScanOptionEvent
    data class ImageFromGallerySelected(val uri: Uri) : ScanOptionEvent
}