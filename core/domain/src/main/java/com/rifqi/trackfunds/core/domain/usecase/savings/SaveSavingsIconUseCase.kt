package com.rifqi.trackfunds.core.domain.usecase.savings

import android.net.Uri

interface SaveSavingsIconUseCase {
    suspend operator fun invoke(imageUri: Uri): String
}