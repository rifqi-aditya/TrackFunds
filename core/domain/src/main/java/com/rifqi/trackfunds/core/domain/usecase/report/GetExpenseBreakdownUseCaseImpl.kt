package com.rifqi.trackfunds.core.domain.usecase.report

import java.time.LocalDateTime
import javax.inject.Inject

class GetExpenseBreakdownUseCaseImpl @Inject constructor(
    private val repository: com.rifqi.trackfunds.core.domain.repository.TransactionRepository
) : GetExpenseBreakdownUseCase {
    override operator fun invoke(startDate: LocalDateTime, endDate: LocalDateTime) =
        repository.getExpenseBreakdownForPeriod(startDate, endDate)
}