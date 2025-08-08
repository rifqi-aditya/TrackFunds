package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.user.model.UpdateProfileParams
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val validateProfileUseCase: ValidateProfileUseCase
) : UpdateProfileUseCase {

    override suspend operator fun invoke(params: UpdateProfileParams): Result<Unit> {
        return runCatching {
            val validationResult = validateProfileUseCase(params.fullName)
            if (!validationResult.isSuccess) {
                throw IllegalArgumentException(validationResult.fullNameError ?: "Invalid input.")
            }

            val originalUser = userRepository.observeProfile().first()
                ?: throw IllegalStateException("User not found or not logged in.")

            val updatedUser = originalUser.copy(
                fullName = params.fullName,
                phoneNumber = params.phoneNumber,
                birthdate = params.birthdate,
            )

            userRepository.saveProfile(
                user = updatedUser,
                newImageUri = params.newImageUri
            ).getOrThrow()
        }
    }
}