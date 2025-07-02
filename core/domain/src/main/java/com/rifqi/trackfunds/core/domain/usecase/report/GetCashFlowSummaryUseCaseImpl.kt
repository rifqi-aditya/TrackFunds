package com.rifqi.trackfunds.core.domain.usecase.report

import java.time.LocalDateTime
import javax.inject.Inject

class GetCashFlowSummaryUseCaseImpl @Inject constructor(
    private val repository: com.rifqi.trackfunds.core.domain.repository.TransactionRepository
) : GetCashFlowSummaryUseCase {
    override operator fun invoke(startDate: LocalDateTime, endDate: LocalDateTime) =
        repository.getCashFlowSummaryForPeriod(startDate, endDate)
}