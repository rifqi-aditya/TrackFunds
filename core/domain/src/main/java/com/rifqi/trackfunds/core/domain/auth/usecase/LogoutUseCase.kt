package com.rifqi.trackfunds.core.domain.auth.usecase

interface LogoutUseCase {
    suspend operator fun invoke(): Result<Unit>
}