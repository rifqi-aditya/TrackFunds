package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface TransactionRepository {
    /**
     * Retrieves a filtered list of transactions.
     * This is the main function to get transactions based on various criteria.
     */
    fun getFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionItem>>

    /**
     * Retrieves a single transaction by its ID.
     */
    fun getTransactionById(transactionId: String): Flow<TransactionItem?>

    /**
     * Inserts a new transaction and updates the corresponding account balance.
     */
    suspend fun insertTransaction(transaction: TransactionItem)

    /**
     * Updates an existing transaction and adjusts account balances accordingly.
     */
    suspend fun updateTransaction(
        transaction: TransactionItem,
        oldAmount: BigDecimal,
        oldAccountId: String
    )

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transaction: TransactionItem)

    /**
     * Executes a transfer between two accounts by creating an expense and an income transaction atomically.
     */
    suspend fun performTransfer(expense: TransactionItem, income: TransactionItem)
}