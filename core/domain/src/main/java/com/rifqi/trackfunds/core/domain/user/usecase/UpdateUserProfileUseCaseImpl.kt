package com.rifqi.trackfunds.core.domain.user.usecase

import android.net.Uri
import com.rifqi.trackfunds.core.domain.common.model.User
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : UpdateUserProfileUseCase {
    override suspend operator fun invoke(user: User, imageUri: Uri?): Result<Unit> {
        return userRepository.createOrUpdateProfile(user, imageUri)
    }
}