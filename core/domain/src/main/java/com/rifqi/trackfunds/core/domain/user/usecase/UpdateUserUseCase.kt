package com.rifqi.trackfunds.core.domain.user.usecase

import com.rifqi.trackfunds.core.domain.user.model.UpdateProfileParams

interface UpdateUserUseCase {
    suspend operator fun invoke(params: UpdateProfileParams): Result<Unit>
}