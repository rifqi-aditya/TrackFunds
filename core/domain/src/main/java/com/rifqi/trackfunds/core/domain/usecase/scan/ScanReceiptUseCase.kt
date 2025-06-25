package com.rifqi.trackfunds.core.domain.usecase.scan

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.ScanResult

interface ScanReceiptUseCase {
    suspend operator fun invoke(imageUri: Uri): ScanResult
}