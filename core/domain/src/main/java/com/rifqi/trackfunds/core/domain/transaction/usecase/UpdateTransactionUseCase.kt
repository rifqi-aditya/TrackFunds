package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.category.model.UpdateTransactionParams

interface UpdateTransactionUseCase {
    suspend operator fun invoke(params: UpdateTransactionParams): Result<Unit>
}