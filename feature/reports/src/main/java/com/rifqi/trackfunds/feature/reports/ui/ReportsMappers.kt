package com.rifqi.trackfunds.feature.reports.ui

import androidx.compose.ui.graphics.Color
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowReport
import com.rifqi.trackfunds.core.domain.reports.model.ExpenseReport
import com.rifqi.trackfunds.core.domain.reports.model.IncomeReport
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.reports.ui.state.CashFlowChartItem
import com.rifqi.trackfunds.feature.reports.ui.state.CashFlowListItem
import com.rifqi.trackfunds.feature.reports.ui.state.CashFlowReportData
import com.rifqi.trackfunds.feature.reports.ui.state.CategorySummaryUiModel
import com.rifqi.trackfunds.feature.reports.ui.state.ExpenseReportData
import com.rifqi.trackfunds.feature.reports.ui.state.IncomeReportData
import java.time.format.TextStyle
import java.util.Locale


val categoryPalette = listOf(
    Color(0xFF4F46E5), // indigo
    Color(0xFF10B981), // emerald
    Color(0xFFF59E0B), // amber
    Color(0xFFEF4444), // red
    Color(0xFF3B82F6), // blue
    Color(0xFF8B5CF6), // violet
    Color(0xFF14B8A6), // teal
    Color(0xFFEC4899), // pink
    Color(0xFF22C55E), // green
    Color(0xFFF97316), // orange
    Color(0xFF06B6D4), // cyan
    Color(0xFF84CC16), // lime
)

private fun pickColorForKey(key: String, palette: List<Color> = categoryPalette): Color {
    val idx = (key.hashCode() and 0x7fffffff) % palette.size
    return palette[idx]
}

fun mapExpenseReport(report: ExpenseReport): ExpenseReportData {
    val summaries = report.categorySummaries.map { s ->
        val key = s.category.id
        CategorySummaryUiModel(
            categoryName = s.category.name,
            totalAmount = s.totalAmount,
            percentage = s.percentage,
            color = pickColorForKey(key)
        )
    }
    return ExpenseReportData(categorySummaries = summaries)
}

fun mapIncomeReport(report: IncomeReport): IncomeReportData {
    val summaries = report.categorySummaries.map { s ->
        val key = s.category.id
        CategorySummaryUiModel(
            categoryName = s.category.name,
            totalAmount = s.totalAmount,
            percentage = s.percentage,
            color = pickColorForKey(key)
        )
    }
    return IncomeReportData(categorySummaries = summaries)
}

fun mapCashFlowReport(report: CashFlowReport): CashFlowReportData {
    val chartItems = report.monthlyFlows.map { flow ->
        CashFlowChartItem(
            monthLabel = flow.period.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            incomeAmount = flow.income,
            expenseAmount = flow.expense
        )
    }

    val listItems = report.monthlyFlows.map { flow ->
        val netFlow = flow.income - flow.expense
        CashFlowListItem(
            monthName = flow.period.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            formattedIncome = formatCurrency(flow.income),
            formattedExpense = formatCurrency(flow.expense),
            formattedNetFlow = formatCurrency(netFlow),
        )
    }

    return CashFlowReportData(chartItems, listItems)
}
