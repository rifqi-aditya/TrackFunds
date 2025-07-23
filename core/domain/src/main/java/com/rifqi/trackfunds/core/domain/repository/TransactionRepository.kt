package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import kotlinx.coroutines.flow.Flow

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
    suspend fun insertTransaction(transaction: TransactionItem): Result<Unit>

    /**
     * Updates an existing transaction.
     */
    suspend fun updateTransaction(transaction: TransactionItem): Result<Unit>

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transactionId: String): Result<Unit>
}