package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface GetFilteredTransactionsUseCase {
    operator fun invoke(filter: TransactionFilter): Flow<List<Transaction>>
}