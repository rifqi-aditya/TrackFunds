package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredTransactionsUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetFilteredTransactionsUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(filter: TransactionFilter): Flow<List<Transaction>> {
        return repository.getFilteredTransactions(filter)
    }
}