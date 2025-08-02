package com.rifqi.trackfunds.core.domain.auth.usecase

interface RegisterUserUseCase {
    suspend operator fun invoke(email: String, pass: String, fullName: String): Result<Unit>
}