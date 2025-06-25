package com.rifqi.trackfunds.core.domain.usecase.scan

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import javax.inject.Inject

class ScanReceiptUseCaseImpl @Inject constructor(
    private val repository: ScanRepository
) : ScanReceiptUseCase {
    override suspend operator fun invoke(imageUri: Uri): ScanResult {
        return repository.scanReceipt(imageUri)
    }
}