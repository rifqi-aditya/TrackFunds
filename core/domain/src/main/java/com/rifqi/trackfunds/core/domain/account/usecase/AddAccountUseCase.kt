package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.AddAccountParams

interface AddAccountUseCase {
    suspend operator fun invoke(params: AddAccountParams): Result<Unit>
}