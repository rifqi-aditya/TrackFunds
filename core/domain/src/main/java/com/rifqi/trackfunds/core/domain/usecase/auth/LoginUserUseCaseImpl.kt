package com.rifqi.trackfunds.core.domain.usecase.auth

import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : LoginUserUseCase {
    // Tidak ada validasi di sini, hanya aksi utama
    override suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        return authRepository.login(email, pass)
    }
}