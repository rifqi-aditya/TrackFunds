package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface GetTransactionsUseCase {
    operator fun invoke(): Flow<List<TransactionItem>>
}