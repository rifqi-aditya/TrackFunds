package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LogoutUserUseCase {
    override suspend operator fun invoke() {
        authRepository.logout()
    }
}