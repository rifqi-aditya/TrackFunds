package com.rifqi.trackfunds.feature.scan.ui.state

import android.net.Uri

data class ScanUiState(
    val isLoading: Boolean = false,
    val capturedImageUri: Uri? = null, // Menyimpan foto yang baru diambil
    val isScanSuccessful: Boolean = false, // Sinyal untuk navigasi kembali
    val error: String? = null
)