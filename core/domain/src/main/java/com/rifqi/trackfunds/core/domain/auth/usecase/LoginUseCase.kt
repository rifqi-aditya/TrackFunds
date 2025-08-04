package com.rifqi.trackfunds.core.domain.auth.usecase

interface LoginUseCase {
    suspend operator fun invoke(email: String, pass: String): Result<Unit>
}