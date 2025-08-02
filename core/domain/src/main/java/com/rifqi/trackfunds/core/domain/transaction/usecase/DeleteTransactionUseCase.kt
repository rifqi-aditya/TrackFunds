package com.rifqi.trackfunds.core.domain.transaction.usecase

interface DeleteTransactionUseCase {
    suspend operator fun invoke(transactionId: String): Result<Unit>
}