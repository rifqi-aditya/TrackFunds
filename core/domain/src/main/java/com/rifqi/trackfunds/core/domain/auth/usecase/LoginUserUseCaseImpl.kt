package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUserUseCase {
    override suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        return authRepository.login(email, pass)
    }
}