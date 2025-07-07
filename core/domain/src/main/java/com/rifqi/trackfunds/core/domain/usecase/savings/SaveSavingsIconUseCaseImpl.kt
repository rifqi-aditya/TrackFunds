package com.rifqi.trackfunds.core.domain.usecase.savings

import android.net.Uri
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import javax.inject.Inject

class SaveSavingsIconUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : SaveSavingsIconUseCase {
    override suspend operator fun invoke(imageUri: Uri): String {
        return repository.copyIconToInternalStorage(imageUri)
    }
}
