package com.rifqi.trackfunds.core.domain.usecase.user

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.UserModel
import com.rifqi.trackfunds.core.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UpdateUserProfileUseCase {
    override suspend operator fun invoke(userModel: UserModel, imageUri: Uri?): Result<Unit> {
        return userRepository.createOrUpdateProfile(userModel, imageUri)
    }
}