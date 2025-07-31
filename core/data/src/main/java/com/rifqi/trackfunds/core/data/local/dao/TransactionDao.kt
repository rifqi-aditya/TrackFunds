package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Upsert
import androidx.sqlite.db.SimpleSQLiteQuery
import com.rifqi.trackfunds.core.data.local.dto.TransactionWithDetailsDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.data.local.entity.TransactionWithDetails
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the transactions table.
 * Handles all database operations related to transactions.
 */
@Dao
interface TransactionDao {


    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    // =================================================================
    // READ (GET) OPERATIONS
    // =================================================================

    /**
     * Gets a single transaction entity by its ID, without any details.
     * Useful for quick internal checks.
     */
    @Query("SELECT * FROM transactions WHERE id = :transactionId AND user_uid = :userUid")
    suspend fun getTransactionById(transactionId: String, userUid: String): TransactionEntity?

    /**
     * Retrieves a specific transaction along with its associated details (category, account).
     * This is a Flow, so it will automatically update when the underlying data changes.
     * @param transactionId The ID of the transaction to retrieve.
     * @param userUid The UID of the user who owns the transaction.
     */
    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :transactionId AND user_uid = :userUid")
    fun getTransactionWithDetails(
        transactionId: String,
        userUid: String
    ): Flow<TransactionWithDetails?>


    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :transactionId AND user_uid = :userUid")
    suspend fun findTransactionWithDetailsById(transactionId: String, userUid: String): TransactionWithDetails?

    /**
     * The main query function that retrieves a list of transactions based on dynamic filters.
     * The userUid filter must be added in the Repository before calling this.
     */
    @Transaction
    @RawQuery(observedEntities = [TransactionEntity::class])
    fun getFilteredTransactionDetailsRaw(query: SimpleSQLiteQuery): Flow<List<TransactionWithDetailsDto>>

    // =================================================================
    // DELETE OPERATIONS
    // =================================================================

    /**
     * Deletes a transaction from the database by its ID for a specific user.
     */
    @Query("DELETE FROM transactions WHERE id = :transactionId AND user_uid = :userUid")
    suspend fun deleteTransactionById(transactionId: String, userUid: String)


    @Query(
        """
        SELECT * FROM transactions 
        WHERE user_uid = :userUid AND savings_goal_id = :goalId 
        ORDER BY date DESC
    """
    )
    fun getTransactionsForGoal(userUid: String, goalId: String): Flow<List<TransactionEntity>>
}