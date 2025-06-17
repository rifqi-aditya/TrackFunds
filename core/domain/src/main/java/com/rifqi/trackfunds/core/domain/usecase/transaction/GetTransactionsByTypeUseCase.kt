package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetTransactionsByTypeUseCase {
    operator fun invoke(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>>
}