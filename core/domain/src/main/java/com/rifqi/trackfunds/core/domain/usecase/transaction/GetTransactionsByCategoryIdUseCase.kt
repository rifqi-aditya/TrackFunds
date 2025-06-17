package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetTransactionsByCategoryIdUseCase {
    operator fun invoke(
        categoryId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>>
}