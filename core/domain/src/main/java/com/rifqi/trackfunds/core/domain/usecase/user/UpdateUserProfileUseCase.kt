package com.rifqi.trackfunds.core.domain.usecase.user

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.User

interface UpdateUserProfileUseCase {
    suspend operator fun invoke(user: User, imageUri: Uri?): Result<Unit>
}