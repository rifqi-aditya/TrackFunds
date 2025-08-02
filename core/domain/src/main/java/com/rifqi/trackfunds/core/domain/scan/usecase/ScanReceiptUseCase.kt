package com.rifqi.trackfunds.core.domain.scan.usecase

import android.net.Uri
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult

interface ScanReceiptUseCase {
    suspend operator fun invoke(imageUri: Uri): Result<ScanResult>
}