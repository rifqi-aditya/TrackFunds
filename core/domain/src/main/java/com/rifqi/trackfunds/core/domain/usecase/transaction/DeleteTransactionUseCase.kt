package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem

interface DeleteTransactionUseCase {
    suspend operator fun invoke(transaction: TransactionItem): Result<Unit>
}