package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredTransactionsUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetFilteredTransactionsUseCase {
    override operator fun invoke(filter: TransactionFilter): Flow<List<Transaction>> {
        return repository.getFilteredTransactions(filter)
    }
}