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
    val category: CategoryItem? = null,
    val account: AccountItem,
    val savingsGoalItem: SavingsGoalItem? = null,
    val transferPairId: String? = null,
)