package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionModel

interface AddTransactionUseCase {
    suspend operator fun invoke(transaction: TransactionModel): Result<Unit>
}