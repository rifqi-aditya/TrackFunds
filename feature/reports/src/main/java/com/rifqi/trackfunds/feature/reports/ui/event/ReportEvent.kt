package com.rifqi.trackfunds.feature.reports.ui.event

import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.YearMonth

sealed interface ReportEvent {
    data class PeriodChanged(val newPeriod: YearMonth) : ReportEvent
    data class BreakdownTypeSelected(val type: TransactionType) : ReportEvent
}