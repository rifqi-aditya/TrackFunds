package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface GetRecentTransactionsUseCase {
    operator fun invoke(limit: Int = 3): Flow<List<TransactionItem>>
}