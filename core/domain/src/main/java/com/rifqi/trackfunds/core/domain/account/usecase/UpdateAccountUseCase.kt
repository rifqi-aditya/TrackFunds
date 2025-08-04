package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.UpdateAccountParams

interface UpdateAccountUseCase {
    suspend operator fun invoke(params: UpdateAccountParams): Result<Unit>
}