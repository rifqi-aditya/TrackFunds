package com.rifqi.trackfunds.core.data.local.dto

import java.math.BigDecimal

data class BudgetWithDetailsDto(
    // Data dari tabel budget
    val budgetId: String,
    val budgetAmount: BigDecimal?,
    val period: String,

    // Data dari tabel kategori (hasil JOIN)
    val categoryId: String,
    val categoryName: String?,
    val categoryIconIdentifier: String?,

    // Data dari transaksi (hasil agregasi SUM)
    val spentAmount: BigDecimal
)