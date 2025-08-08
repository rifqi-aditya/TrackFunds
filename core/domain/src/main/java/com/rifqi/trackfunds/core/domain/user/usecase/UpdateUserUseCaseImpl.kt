package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.user.model.UpdateProfileParams
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val validateProfileUseCase: ValidateProfileUseCase
) : UpdateUserUseCase {

    override suspend operator fun invoke(params: UpdateProfileParams): Result<Unit> {
        return runCatching {
            // 1. Validasi input terlebih dahulu
            val validationResult = validateProfileUseCase(params.fullName)
            if (!validationResult.isSuccess) {
                throw IllegalArgumentException(validationResult.fullNameError)
            }

        }
    }
}