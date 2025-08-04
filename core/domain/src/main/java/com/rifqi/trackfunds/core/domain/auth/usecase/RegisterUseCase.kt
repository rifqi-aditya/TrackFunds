package com.rifqi.trackfunds.core.domain.auth.usecase

interface RegisterUseCase {
    suspend operator fun invoke(email: String, pass: String, fullName: String): Result<Unit>
}