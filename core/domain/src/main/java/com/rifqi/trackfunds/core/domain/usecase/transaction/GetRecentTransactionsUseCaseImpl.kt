package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentTransactionsUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetRecentTransactionsUseCase {
    override operator fun invoke(limit: Int): Flow<List<TransactionItem>> {
        return repository.getRecentTransactions(limit)
    }
}