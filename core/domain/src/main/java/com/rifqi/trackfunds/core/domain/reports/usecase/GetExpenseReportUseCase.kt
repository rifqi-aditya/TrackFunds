package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.reports.model.ExpenseReport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetExpenseReportUseCase {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<ExpenseReport>
}