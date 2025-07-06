package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionItem(
    val id: String = UUID.randomUUID().toString(),
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,
    val description: String,
    val categoryId: String? = null,
    val categoryName: String,
    val categoryIconIdentifier: String? = null,
    val accountId: String,
    val accountName: String,
    val accountIconIdentifier: String? = null,
    val transferPairId: String? = null,
)