package com.rifqi.trackfunds.core.domain.usecase.auth

import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUserUseCase {

    override suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty."))
        }
        return authRepository.login(email, pass)
    }
}