package com.rifqi.trackfunds.core.domain.usecase.auth

import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LogoutUserUseCase {
    override suspend operator fun invoke() {
        authRepository.logout()
    }
}