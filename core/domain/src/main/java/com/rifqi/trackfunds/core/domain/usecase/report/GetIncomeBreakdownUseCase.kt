package com.rifqi.trackfunds.core.domain.usecase.report

import com.rifqi.trackfunds.core.domain.model.CategorySpending
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetIncomeBreakdownUseCase {
    operator fun invoke(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySpending>>
}