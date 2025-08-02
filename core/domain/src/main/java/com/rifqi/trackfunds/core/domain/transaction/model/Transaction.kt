package com.rifqi.trackfunds.core.domain.transaction.model

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,
    val description: String,
    /**
     * The category associated with this transaction. Can be null if the transaction
     * is not categorized or if it's a transfer between accounts.
     */
    val category: Category? = null,
    val account: Account,
    /** The savings goal this transaction is associated with, if any. */
    val savingsGoal: SavingsGoal? = null,
    /** If this is part of a transfer, this ID links it to the other transaction in the pair. */
    val transferPairId: String? = null,
    val receiptImageUrl: String? = null,
    /** A list of detailed line items for this transaction, e.g., items on a receipt. */
    val items: List<TransactionItem> = emptyList()
)