package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.category.model.AddTransactionParams

interface AddTransactionUseCase {
    suspend operator fun invoke(params: AddTransactionParams): Result<Unit>
}