package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem

interface PerformTransferUseCase {
    suspend operator fun invoke(expense: TransactionItem, income: TransactionItem): Result<Unit>
}