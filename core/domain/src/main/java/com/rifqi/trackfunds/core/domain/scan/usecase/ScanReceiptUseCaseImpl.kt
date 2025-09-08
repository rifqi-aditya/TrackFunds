package com.rifqi.trackfunds.core.domain.scan.usecase

import android.net.Uri
import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult
import com.rifqi.trackfunds.core.domain.scan.repository.ScanRepository
import javax.inject.Inject

class ScanReceiptUseCaseImpl @Inject constructor(
    private val repository: ScanRepository,
    private val appPrefsRepository: AppPrefsRepository
) : ScanReceiptUseCase {

    override suspend operator fun invoke(imageUri: Uri): Result<ScanResult> {
        return runCatching {
            val userUid = appPrefsRepository.requireActiveUserId()
            val receiptText = repository.extractTextFromImage(imageUri).getOrThrow()

            val receiptDetails = repository.analyzeReceiptText(receiptText, userUid).getOrThrow()

            receiptDetails.copy(receiptImageUri = imageUri)
        }
    }
}