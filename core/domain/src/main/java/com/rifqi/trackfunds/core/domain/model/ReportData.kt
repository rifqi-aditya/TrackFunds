package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class CategorySpending(
    val categoryName: String,
    val totalAmount: BigDecimal
)

data class CashFlowSummary(
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val netCashFlow: BigDecimal
)