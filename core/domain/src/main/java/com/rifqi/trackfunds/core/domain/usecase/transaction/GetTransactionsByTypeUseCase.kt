package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface GetTransactionsByTypeUseCase {
    operator fun invoke(type: TransactionType): Flow<List<TransactionItem>>
}