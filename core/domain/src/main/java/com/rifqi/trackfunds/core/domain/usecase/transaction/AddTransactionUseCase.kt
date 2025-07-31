package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.params.AddTransactionParams

interface AddTransactionUseCase {
    suspend operator fun invoke(params: AddTransactionParams): Result<Unit>
}