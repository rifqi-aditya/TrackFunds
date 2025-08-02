package com.rifqi.trackfunds.core.domain.user.usecase

import android.net.Uri
import com.rifqi.trackfunds.core.domain.common.model.User

interface UpdateUserProfileUseCase {
    suspend operator fun invoke(user: User, imageUri: Uri?): Result<Unit>
}