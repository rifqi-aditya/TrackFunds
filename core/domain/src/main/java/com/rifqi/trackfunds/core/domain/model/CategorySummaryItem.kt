package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class CategorySummaryItem(
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val transactionType: TransactionType,
    val totalAmount: BigDecimal,
)