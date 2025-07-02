package com.rifqi.trackfunds.core.data.local.dto

import androidx.room.ColumnInfo
import java.math.BigDecimal

// DTO untuk rincian pengeluaran/pemasukan per kategori
data class CategorySpendingDto(
    val categoryName: String,
    val totalAmount: BigDecimal
)

// DTO untuk ringkasan alur kas
data class CashFlowDto(
    @ColumnInfo(name = "total_income")
    val totalIncome: BigDecimal?,

    @ColumnInfo(name = "total_expense")
    val totalExpense: BigDecimal?
)