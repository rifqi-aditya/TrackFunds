package com.rifqi.trackfunds.core.domain.auth.usecase

import com.rifqi.trackfunds.core.domain.auth.model.RegisterParams

interface RegisterUseCase {
    suspend operator fun invoke(params: RegisterParams): Result<String>
}