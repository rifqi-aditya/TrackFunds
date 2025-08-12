package com.rifqi.trackfunds.feature.reports.ui.state

import androidx.compose.ui.graphics.Color
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption
import java.math.BigDecimal
import java.time.LocalDate

data class ReportsUiState(
    // General UI State
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeSheet: ActiveSheet? = null,
    val activeDialog: ActiveDialog? = null,

    // Tab and Date Range Selection
    val selectedTabIndex: Int = 0,
    val selectedDateOption: DateRangeOption = DateRangeOption.THIS_MONTH,
    val formattedDateRange: String = "This Month",
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null,
    val selectedCashFlowPeriod: CashFlowPeriodOption = CashFlowPeriodOption.LAST_3_MONTHS,

    // Report Specific Data
    val expenseReportData: ExpenseReportData = ExpenseReportData(),
    val incomeReportData: IncomeReportData = IncomeReportData(),
    val cashFlowReportData: CashFlowReportData = CashFlowReportData(),
)

enum class ActiveDialog { DATE_RANGE }
enum class ActiveSheet { DATE_RANGE, CASH_FLOW }

data class CategorySummaryUiModel(
    val categoryName: String,
    val totalAmount: BigDecimal,
    val percentage: Float,
    val color: Color
)

data class ExpenseReportData(
    val categorySummaries: List<CategorySummaryUiModel> = emptyList()
)

data class IncomeReportData(
    val categorySummaries: List<CategorySummaryUiModel> = emptyList()
)

data class CashFlowChartItem(
    val monthLabel: String,
    val incomeAmount: BigDecimal,
    val expenseAmount: BigDecimal
)

data class CashFlowListItem(
    val monthName: String,
    val formattedIncome: String,
    val formattedExpense: String,
    val formattedNetFlow: String,
)

data class CashFlowReportData(
    val chartItems: List<CashFlowChartItem> = emptyList(),
    val listItems: List<CashFlowListItem> = emptyList()
)
