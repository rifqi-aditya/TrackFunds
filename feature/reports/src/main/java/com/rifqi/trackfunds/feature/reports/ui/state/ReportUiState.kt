package com.rifqi.trackfunds.feature.reports.ui.state

import com.rifqi.trackfunds.core.domain.model.CashFlowSummary
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.YearMonth

data class ReportUiState(
    val isLoading: Boolean = true,
    val currentPeriod: YearMonth = YearMonth.now(),
    val cashFlowSummary: CashFlowSummary? = null,
    val expenseBreakdown: List<CategorySpending> = emptyList(),
    val incomeBreakdown: List<CategorySpending> = emptyList(),
    val error: String? = null,
    val showMonthPickerDialog: Boolean = false,
    val activeBreakdownType: TransactionType = TransactionType.EXPENSE,
)