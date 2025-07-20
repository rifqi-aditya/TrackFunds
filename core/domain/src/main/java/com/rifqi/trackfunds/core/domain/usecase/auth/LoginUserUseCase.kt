package com.rifqi.trackfunds.core.domain.usecase.auth

interface LoginUserUseCase {
    suspend operator fun invoke(email: String, pass: String): Result<Unit>
}