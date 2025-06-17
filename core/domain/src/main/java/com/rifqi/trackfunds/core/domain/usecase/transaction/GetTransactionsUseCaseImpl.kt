package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetTransactionsUseCase {

    override operator fun invoke(): Flow<List<TransactionItem>> {
        return transactionRepository.getTransactions()
    }
}