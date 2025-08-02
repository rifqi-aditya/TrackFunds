package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import kotlinx.coroutines.flow.Flow

interface GetFilteredTransactionsUseCase {
    operator fun invoke(filter: TransactionFilter): Flow<List<Transaction>>
}