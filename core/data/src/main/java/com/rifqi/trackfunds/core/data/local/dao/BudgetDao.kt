package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.dto.BudgetWithDetailsDto
import com.rifqi.trackfunds.core.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Data Access Object for the budgets table.
 */
@Dao
interface BudgetDao {

    /**
     * Fetches budgets with details for a specific user and month.
     * @param startOfMonth The first day of the month (e.g., 2025-07-01).
     * @param endOfMonth The last moment of the last day of the month (e.g., 2025-07-31 23:59:59).
     * @param userUid The ID of the current user.
     */
    @Query(
        """
        SELECT
            b.id as budgetId, b.amount as budgetAmount, b.period as period,
            c.id as categoryId, c.name as categoryName, c.icon_identifier as categoryIconIdentifier,
            COALESCE(SUM(t.amount), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        LEFT JOIN transactions AS t ON t.category_id = c.id 
                                   AND t.type = 'EXPENSE' 
                                   AND t.user_uid = :userUid
                                   AND t.date BETWEEN :startOfMonth AND :endOfMonth
        WHERE b.period = :startOfMonth AND b.user_uid = :userUid
        GROUP BY b.id, c.id
    """
    )
    fun getBudgetsWithDetails(
        startOfMonth: LocalDate,
        endOfMonth: LocalDateTime,
        userUid: String
    ): Flow<List<BudgetWithDetailsDto>>

    /**
     * Fetches a single budget with details by its ID for a specific user.
     */
    @Transaction
    @Query(
        """
        SELECT
            b.id as budgetId, b.amount as budgetAmount, b.period as period,
            c.id as categoryId, c.name as categoryName, c.icon_identifier as categoryIconIdentifier,
            COALESCE((
                SELECT SUM(t.amount) 
                FROM transactions AS t 
                WHERE t.category_id = b.category_id 
                AND t.type = 'EXPENSE'
                AND t.user_uid = :userUid
            ), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        WHERE b.id = :budgetId AND b.user_uid = :userUid
    """
    )
    suspend fun getBudgetWithDetailsById(budgetId: String, userUid: String): BudgetWithDetailsDto?

    /**
     * Fetches the top N budgets for a specific user and month.
     */
    @Transaction
    @Query(
        """
        SELECT
            b.id as budgetId, b.amount as budgetAmount, b.period as period,
            c.id as categoryId, c.name as categoryName, c.icon_identifier as categoryIconIdentifier,
            COALESCE(SUM(t.amount), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        LEFT JOIN transactions AS t ON t.category_id = c.id 
                                   AND t.type = 'EXPENSE' 
                                   AND t.user_uid = :userUid
                                   AND t.date BETWEEN :startOfMonth AND :endOfMonth
        WHERE b.period = :startOfMonth AND b.user_uid = :userUid
        GROUP BY b.id
        ORDER BY spentAmount / b.amount DESC
        LIMIT :limit
    """
    )
    fun getTopBudgetsWithDetails(
        startOfMonth: LocalDate,
        endOfMonth: LocalDateTime,
        limit: Int,
        userUid: String
    ): Flow<List<BudgetWithDetailsDto>>


    @Query("SELECT id FROM budgets WHERE user_uid = :userUid AND category_id = :categoryId AND period = :period LIMIT 1")
    suspend fun findBudgetId(userUid: String, categoryId: String, period: LocalDate): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :budgetId AND user_uid = :userUid")
    suspend fun deleteBudgetById(budgetId: String, userUid: String)
}