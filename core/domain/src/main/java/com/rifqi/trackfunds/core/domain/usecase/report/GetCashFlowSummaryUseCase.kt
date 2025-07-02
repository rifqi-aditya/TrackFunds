package com.rifqi.trackfunds.core.domain.usecase.report

import com.rifqi.trackfunds.core.domain.model.CashFlowSummary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface GetCashFlowSummaryUseCase {
    operator fun invoke(startDate: LocalDateTime, endDate: LocalDateTime): Flow<CashFlowSummary>
}