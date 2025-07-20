package com.rifqi.trackfunds.core.domain.usecase.auth

import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RegisterUserUseCase {

    override suspend operator fun invoke(email: String, pass: String): Result<Unit> {
        // Anda bisa menambahkan validasi yang lebih kompleks di sini
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password cannot be empty."))
        }
        if (pass.length < 6) {
            return Result.failure(Exception("Password must be at least 6 characters long."))
        }
        return authRepository.register(email, pass)
    }
}