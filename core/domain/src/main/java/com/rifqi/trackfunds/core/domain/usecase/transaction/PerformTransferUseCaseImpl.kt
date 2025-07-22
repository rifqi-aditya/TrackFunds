package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import javax.inject.Inject

class PerformTransferUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : PerformTransferUseCase {
    override suspend operator fun invoke(expense: Transaction, income: Transaction) {
        repository.performTransfer(expense, income)
    }
}