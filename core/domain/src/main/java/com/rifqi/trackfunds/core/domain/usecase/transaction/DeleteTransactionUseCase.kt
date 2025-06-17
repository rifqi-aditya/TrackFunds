package com.rifqi.trackfunds.core.domain.usecase.transaction

interface DeleteTransactionUseCase {
    suspend operator fun invoke(transactionId: String)
}