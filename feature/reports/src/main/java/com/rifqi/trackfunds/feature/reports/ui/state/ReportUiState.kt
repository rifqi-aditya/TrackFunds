package com.rifqi.trackfunds.feature.reports.ui.state

import com.rifqi.trackfunds.core.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.common.model.CashFlowSummary
import com.rifqi.trackfunds.core.domain.common.model.CategorySpending
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.time.LocalDate
import java.time.YearMonth

data class ReportUiState(
    val isLoading: Boolean = true,
    val currentPeriod: YearMonth = YearMonth.now(),
    val dateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val cashFlowSummary: CashFlowSummary? = null,
    val expenseBreakdown: List<CategorySpending> = emptyList(),
    val incomeBreakdown: List<CategorySpending> = emptyList(),
    val savingsBreakdown: List<CategorySpending> = emptyList(),
    val error: String? = null,
    val showMonthPickerDialog: Boolean = false,
    val selectedDateOption: DateRangeOption = DateRangeOption.LAST_30_DAYS,
    val showPeriodSheet: Boolean = false,
    val activeBreakdownType: TransactionType = TransactionType.EXPENSE,
    val showDatePicker: Boolean = false,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null
)