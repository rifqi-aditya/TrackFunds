package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.reports.model.IncomeReport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetIncomeReportUseCase {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<IncomeReport>
}