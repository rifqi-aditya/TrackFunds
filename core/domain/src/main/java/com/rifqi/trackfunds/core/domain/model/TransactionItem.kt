package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionItem(
    val id: String = UUID.randomUUID().toString(),
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,
    val note: String,
    val categoryId: String? = null,
    val categoryName: String,
    val iconIdentifier: String?,
    val accountId: String,
    val accountName: String,
    val transferPairId: String? = null,
)