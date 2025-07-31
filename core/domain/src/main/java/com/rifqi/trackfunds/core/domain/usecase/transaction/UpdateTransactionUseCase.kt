package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.params.UpdateTransactionParams

interface UpdateTransactionUseCase {
    suspend operator fun invoke(params: UpdateTransactionParams): Result<Unit>
}