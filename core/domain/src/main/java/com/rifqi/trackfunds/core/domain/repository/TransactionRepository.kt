package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    /**
     * Retrieves a filtered list of transactions.
     * This is the main function to get transactions based on various criteria.
     */
    fun getFilteredTransactions(filter: TransactionFilter, userUid: String): Flow<List<Transaction>>

    /**
     * Retrieves a single transaction by its ID.
     */
    fun getTransactionWithDetails(transactionId: String, userUid: String): Flow<Transaction?>

    /**
     * Retrieves a single transaction with its details by ID.
     * This is a suspend function to be used in a coroutine context.
     */
    suspend fun findTransactionWithDetailsById(transactionId: String, userUid: String): Transaction?

    /**
     * Saves a transaction to the database.
     * This can be used for both creating new transactions and updating existing ones.
     */
    suspend fun saveTransaction(transaction: Transaction, userUid: String): Result<Unit>

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transactionId: String): Result<Unit>

    /**
     * Retrieves all transactions associated with a specific savings goal.
     */
    fun getTransactionsForGoal(userUid: String, goalId: String): Flow<List<Transaction>>
}