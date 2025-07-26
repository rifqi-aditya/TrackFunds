package com.rifqi.trackfunds.core.domain.usecase.user

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.UserModel

interface UpdateUserProfileUseCase {
    suspend operator fun invoke(userModel: UserModel, imageUri: Uri?): Result<Unit>
}