package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionModel

interface PerformTransferUseCase {
    suspend operator fun invoke(expense: TransactionModel, income: TransactionModel): Result<Unit>
}