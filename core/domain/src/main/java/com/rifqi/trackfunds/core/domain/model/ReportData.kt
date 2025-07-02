package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

// Model untuk rincian pengeluaran/pemasukan per kategori
data class CategorySpending(
    val categoryName: String,
    val totalAmount: BigDecimal
)

// Model untuk ringkasan alur kas
data class CashFlowSummary(
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val netCashFlow: BigDecimal
)