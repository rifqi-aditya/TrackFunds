package com.rifqi.trackfunds.feature.scan.ui.event

import android.content.Context
import android.net.Uri

sealed interface ScanEvent {
    data class PhotoTaken(val uri: Uri) : ScanEvent
    data class ConfirmPhoto(val context: Context) : ScanEvent
    data object RetakePhoto : ScanEvent
}