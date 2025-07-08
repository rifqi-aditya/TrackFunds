package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : DeleteTransactionUseCase {

    override suspend operator fun invoke(transaction: TransactionItem) {
        transactionRepository.deleteTransaction(transaction)
    }
}