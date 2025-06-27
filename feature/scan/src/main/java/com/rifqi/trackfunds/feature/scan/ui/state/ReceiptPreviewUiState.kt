package com.rifqi.trackfunds.feature.scan.ui.state

import android.net.Uri

data class ReceiptPreviewUiState(
    val isLoading: Boolean = false,
    val isScanSuccessful: Boolean = false,
    val error: String? = null,
    val imageUri: Uri? = null
)