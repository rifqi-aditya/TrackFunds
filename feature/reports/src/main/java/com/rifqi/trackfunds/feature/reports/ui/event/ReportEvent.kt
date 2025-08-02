package com.rifqi.trackfunds.feature.reports.ui.event

import com.rifqi.trackfunds.core.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.time.LocalDate
import java.time.YearMonth

sealed interface ReportEvent {
    data class PeriodChanged(val newPeriod: YearMonth) : ReportEvent
    data class BreakdownTypeSelected(val type: TransactionType) : ReportEvent
    data object ChangePeriodClicked : ReportEvent
    data object PeriodSheetDismissed : ReportEvent
    data class DateOptionSelected(val option: DateRangeOption) : ReportEvent
    data object CustomDateRangeClicked : ReportEvent
    data object DatePickerDismissed : ReportEvent
    data class DateRangeSelected(val startDate: LocalDate, val endDate: LocalDate) : ReportEvent
}