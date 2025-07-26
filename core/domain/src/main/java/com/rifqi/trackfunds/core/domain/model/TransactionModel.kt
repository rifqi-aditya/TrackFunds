package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a single financial transaction.
 *
 * This data class holds all the relevant information about a transaction,
 * including its unique identifier, amount, type, date, description,
 * associated category, account, and optional details like savings goal,
 * transfer information, payment method, receipt image, and line items.
 *
 * @property id The unique identifier of the transaction. Defaults to a randomly generated UUID.
 * @property amount The monetary value of the transaction.
 * @property type The type of the transaction (e.g., INCOME, EXPENSE, TRANSFER).
 * @property date The date and time when the transaction occurred.
 * @property description A textual description of the transaction.
 * @property category The category associated with this transaction, if applicable.
 * @property account The account to or from which this transaction was made.
 * @property savingsGoalModel The savings goal this transaction contributes to or deducts from, if applicable.
 * @property transferPairId If this transaction is part of a transfer, this ID links it to the corresponding transaction in the other account.
 * @property paymentMethod The method used for payment (e.g., "Credit Card", "Cash").
 * @property receiptImageUrl The URL of an image of the receipt for this transaction, if available.
 * @property receiptItemModels A list of individual items that make up this transaction, if applicable (e.g., for a grocery bill).
 */
data class TransactionModel(
    val id: String = UUID.randomUUID().toString(),
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDateTime,
    val description: String,
    /**
     * The category associated with this transaction. Can be null if the transaction
     * is not categorized or if it's a transfer between accounts.
     */
    val category: CategoryModel? = null,
    val account: AccountModel,
    /** The savings goal this transaction is associated with, if any. */
    val savingsGoalModel: SavingsGoalModel? = null,
    /** If this is part of a transfer, this ID links it to the other transaction in the pair. */
    val transferPairId: String? = null,
    val paymentMethod: String? = null,
    val receiptImageUrl: String? = null,
    /** A list of detailed line items for this transaction, e.g., items on a receipt. */
    val receiptItemModels: List<ReceiptItemModel> = emptyList()
)