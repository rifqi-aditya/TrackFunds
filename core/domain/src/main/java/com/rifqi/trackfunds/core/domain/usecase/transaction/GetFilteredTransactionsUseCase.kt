package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface GetFilteredTransactionsUseCase {
    operator fun invoke(filter: TransactionFilter): Flow<List<TransactionItem>>
}