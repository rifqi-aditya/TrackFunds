package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.rifqi.trackfunds.core.data.local.dto.CashFlowDto
import com.rifqi.trackfunds.core.data.local.dto.CategorySpendingDto
import com.rifqi.trackfunds.core.data.local.dto.TransactionDetailDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for the transactions table.
 * Handles all database operations related to transactions.
 */
@Dao
interface TransactionDao {

    // =================================================================
    // CREATE / UPDATE OPERATIONS
    // =================================================================

    /**
     * Inserts a single transaction. If a transaction with the same ID exists, it gets replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    /**
     * Updates an existing transaction.
     */
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    // =================================================================
    // READ (GET) OPERATIONS
    // =================================================================

    /**
     * Gets a single transaction entity by its ID, without any details.
     * Useful for quick internal checks.
     */
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: String): TransactionEntity?

    /**
     * Gets a single transaction with its full details including category, account, and savings goal info.
     */
    @Transaction
    @Query(
        """
        SELECT 
            t.*,
            c.name AS category_name, 
            c.icon_identifier AS category_icon_identifier,
            a.name AS account_name,
            a.icon_identifier AS account_icon_identifier,
            a.balance AS account_balance,
            s.id AS savings_goal_id,
            s.name AS savings_goal_name,
            s.icon_identifier AS savings_goal_icon_identifier,
            s.target_date AS savings_goal_target_date,
            s.target_amount AS savings_goal_target_amount,
            s.current_amount AS savings_goal_current_amount,
            CASE WHEN s.current_amount >= s.target_amount THEN 1 ELSE 0 END AS savings_goal_is_achieved
        FROM transactions AS t
        LEFT JOIN categories AS c ON t.category_id = c.id
        INNER JOIN accounts AS a ON t.account_id = a.id
        LEFT JOIN savings_goals AS s ON t.savings_goal_id = s.id
        WHERE t.id = :transactionId
    """
    )
    fun getTransactionWithDetailsById(transactionId: String): Flow<TransactionDetailDto?>

    /**
     * The main query function that retrieves a list of transactions based on dynamic filters.
     * NOTE: The implementation for this is in the Repository, not here. This is the DAO interface.
     */
    @Transaction
    @RawQuery(observedEntities = [TransactionEntity::class])
    fun getFilteredTransactionDetailsRaw(query: SimpleSQLiteQuery): Flow<List<TransactionDetailDto>>

    // =================================================================
    // AGGREGATION / REPORTING OPERATIONS
    // =================================================================

    /**
     * Calculates spending totals for each category within a given date range.
     */
    @Query(
        """
        SELECT c.name as categoryName, SUM(t.amount) as totalAmount
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        WHERE t.type = 'EXPENSE' AND t.date BETWEEN :startDate AND :endDate
        GROUP BY c.name
        ORDER BY totalAmount DESC
    """
    )
    fun getExpenseBreakdown(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySpendingDto>>

    /**
     * Calculates income totals for each category within a given date range.
     */
    @Query(
        """
        SELECT c.name as categoryName, SUM(t.amount) as totalAmount
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        WHERE t.type = 'INCOME' AND t.date BETWEEN :startDate AND :endDate
        GROUP BY c.name
        ORDER BY totalAmount DESC
    """
    )
    fun getIncomeBreakdown(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySpendingDto>>

    /**
     * Calculates the total income and total expense for a given date range.
     */
    @Query(
        """
        SELECT
            (SELECT SUM(amount) FROM transactions WHERE type = 'INCOME' AND date BETWEEN :startDate AND :endDate) as total_income,
            (SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :startDate AND :endDate) as total_expense
    """
    )
    fun getCashFlowSummary(startDate: LocalDateTime, endDate: LocalDateTime): Flow<CashFlowDto>

    // =================================================================
    // DELETE OPERATIONS
    // =================================================================

    /**
     * Deletes a transaction from the database by its ID.
     */
    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: String)
}