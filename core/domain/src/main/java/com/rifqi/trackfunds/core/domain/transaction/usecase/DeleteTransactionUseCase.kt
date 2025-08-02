package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction

interface DeleteTransactionUseCase {
    suspend operator fun invoke(transaction: Transaction): Result<Unit>
}