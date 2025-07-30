package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import kotlinx.coroutines.flow.Flow

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
    suspend fun insertTransaction(transaction: Transaction): Result<Unit>

    /**
     * Updates an existing transaction.
     */
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transactionId: String): Result<Unit>

    /**
     * Inserts line items associated with a transaction.
     */
    suspend fun insertLineItems(receiptItemModels: List<ReceiptItemModel>, transactionId: String): Result<Unit>

    /**
     * Retrieves all transactions associated with a specific savings goal.
     */
    fun getTransactionsForGoal(userUid: String, goalId: String): Flow<List<Transaction>>
}