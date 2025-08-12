package com.rifqi.trackfunds.feature.reports.ui.event

import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption
import java.time.LocalDate

sealed interface ReportsEvent {
    data class TabSelected(val index: Int) : ReportsEvent
    data object ExportClicked : ReportsEvent

    // Trigger & sheet
    data object FilterTriggerClicked : ReportsEvent
    data object SheetDismissed : ReportsEvent
    data object DialogDismissed : ReportsEvent

    // Aksi pilihan
    data class PeriodOptionSelected(val option: DateRangeOption) : ReportsEvent
    data class DateRangeConfirmed(val startDate: LocalDate, val endDate: LocalDate) : ReportsEvent
    data class CashFlowPeriodSelected(val option: CashFlowPeriodOption) : ReportsEvent
}