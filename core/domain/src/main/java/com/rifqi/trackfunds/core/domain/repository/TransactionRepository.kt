package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    /**
     * Retrieves a filtered list of transactions.
     * This is the main function to get transactions based on various criteria.
     */
    fun getFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionModel>>

    /**
     * Retrieves a single transaction by its ID.
     */
    fun getTransactionById(transactionId: String): Flow<TransactionModel?>

    /**
     * Inserts a new transaction and updates the corresponding account balance.
     */
    suspend fun insertTransaction(transaction: TransactionModel): Result<Unit>

    /**
     * Updates an existing transaction.
     */
    suspend fun updateTransaction(transaction: TransactionModel): Result<Unit>

    /**
     * Deletes a transaction and reverts the account balance.
     */
    suspend fun deleteTransaction(transactionId: String): Result<Unit>

    /**
     * Inserts line items associated with a transaction.
     */
    suspend fun insertLineItems(receiptItemModels: List<ReceiptItemModel>, transactionId: String): Result<Unit>
}