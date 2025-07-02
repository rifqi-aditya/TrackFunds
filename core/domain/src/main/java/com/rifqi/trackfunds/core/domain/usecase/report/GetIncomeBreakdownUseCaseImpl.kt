package com.rifqi.trackfunds.core.domain.usecase.report

import java.time.LocalDateTime
import javax.inject.Inject

class GetIncomeBreakdownUseCaseImpl @Inject constructor(
    private val repository: com.rifqi.trackfunds.core.domain.repository.TransactionRepository
) : GetIncomeBreakdownUseCase {
    override operator fun invoke(startDate: LocalDateTime, endDate: LocalDateTime) =
        repository.getIncomeBreakdownForPeriod(startDate, endDate)
}