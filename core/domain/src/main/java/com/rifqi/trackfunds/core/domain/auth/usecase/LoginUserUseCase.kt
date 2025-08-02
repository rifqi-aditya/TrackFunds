package com.rifqi.trackfunds.core.domain.auth.usecase

interface LoginUserUseCase {
    suspend operator fun invoke(email: String, pass: String): Result<Unit>
}