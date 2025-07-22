package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface TransactionRepository {
    /**
     * Retrieves a filtered list of transactions.
     * This is the main function to get transactions based on various criteria.
     */
    fun getFilteredTransactions(filter: TransactionFilter): Flow<List<Transaction>>

    /**
     * Retrieves a single transaction by its ID.
     */
    fun getTransactionById(transactionId: String): Flow<Transaction?>

    /**
     * Inserts a new transaction and updates the corresponding account balance.
     */
    suspend fun insertTransaction(transaction: Transaction)

    /**
     * Updates an existing transaction and adjusts account balances accordingly.
     */
    suspend fun updateTransaction(
        transaction: Transaction,
        oldAmount: BigDecimal,
        oldAccountId: String
    )

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transaction: Transaction)

    /**
     * Executes a transfer between two accounts by creating an expense and an income transaction atomically.
     */
    suspend fun performTransfer(expense: Transaction, income: Transaction)
}