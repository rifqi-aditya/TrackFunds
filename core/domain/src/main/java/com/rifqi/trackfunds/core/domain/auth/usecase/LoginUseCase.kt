package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.model.LoginParams

interface LoginUseCase {
    suspend operator fun invoke(params: LoginParams): Result<Unit>
}