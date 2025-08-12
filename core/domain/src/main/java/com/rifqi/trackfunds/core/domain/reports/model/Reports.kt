package com.rifqi.trackfunds.core.domain.reports.model

import com.rifqi.trackfunds.core.domain.category.model.Category
import java.math.BigDecimal
import java.time.YearMonth

data class ExpenseReport(
    val categorySummaries: List<CategorySummary>
)

data class IncomeReport(
    val categorySummaries: List<CategorySummary>
)

data class CashFlowReport(
    val monthlyFlows: List<MonthlyFlow>
)

data class CategorySummary(
    val category: Category,
    val totalAmount: BigDecimal,
    val percentage: Float
)

data class MonthlyFlow(
    val period: YearMonth,
    val income: BigDecimal,
    val expense: BigDecimal
)