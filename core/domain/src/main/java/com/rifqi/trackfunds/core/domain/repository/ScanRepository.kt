package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.ScanResult

interface ScanRepository {
    suspend fun scanReceipt(imageUri: Uri): ScanResult
}