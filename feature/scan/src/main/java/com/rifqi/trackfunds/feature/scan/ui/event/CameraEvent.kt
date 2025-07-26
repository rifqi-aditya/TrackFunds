package com.rifqi.trackfunds.feature.scan.ui.event

import android.net.Uri

sealed interface CameraEvent {
    data class PhotoTaken(val uri: Uri) : CameraEvent
}