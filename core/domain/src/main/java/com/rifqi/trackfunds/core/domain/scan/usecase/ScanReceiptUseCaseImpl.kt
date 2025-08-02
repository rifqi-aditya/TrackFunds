package com.rifqi.trackfunds.core.domain.scan.usecase

import android.net.Uri
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult
import com.rifqi.trackfunds.core.domain.scan.repository.ScanRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ScanReceiptUseCaseImpl @Inject constructor(
    private val repository: ScanRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ScanReceiptUseCase {

    override suspend operator fun invoke(imageUri: Uri): Result<ScanResult> {
        return runCatching {
            val userUid = userPreferencesRepository.userUid.first()
                ?: throw IllegalStateException("User not logged in.")

            val receiptText = repository.extractTextFromImage(imageUri).getOrThrow()

            val receiptDetails = repository.analyzeReceiptText(receiptText, userUid).getOrThrow()

            receiptDetails.copy(receiptImageUri = imageUri)
        }
    }
}