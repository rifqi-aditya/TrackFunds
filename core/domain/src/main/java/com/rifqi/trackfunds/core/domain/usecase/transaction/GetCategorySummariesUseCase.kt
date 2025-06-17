package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetCategorySummariesUseCase {
    operator fun invoke(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySummaryItem>>
}