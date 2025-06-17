package com.rifqi.trackfunds.core.data.local.dto

import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal

data class CategoryTransactionSummaryDto(
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val transactionType: TransactionType,
    val totalAmount: BigDecimal
)