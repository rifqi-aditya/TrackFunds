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

@Dao
interface BudgetDao {

    @Query(
        """
        SELECT
            b.id as budgetId,
            b.amount as budgetAmount,
            b.period as period,
            c.id as categoryId,
            c.name as categoryName,
            c.icon_identifier as categoryIconIdentifier,
            COALESCE(SUM(t.amount), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        LEFT JOIN transactions AS t ON t.category_id = c.id 
                                   AND t.type = 'EXPENSE' 
                                   AND strftime('%Y-%m', t.date / 1000, 'unixepoch') = b.period
                                   AND t.user_uid = :userUid 
        WHERE b.period = :period AND b.user_uid = :userUid
        GROUP BY b.id, c.id
    """
    )
    fun getBudgetsWithDetails(period: String, userUid: String): Flow<List<BudgetWithDetailsDto>>

    @Transaction
    @Query(
        """
        SELECT
            b.id as budgetId,
            b.amount as budgetAmount,
            b.period as period,
            c.id as categoryId,
            c.name as categoryName,
            c.icon_identifier as categoryIconIdentifier,
            COALESCE((
                SELECT SUM(t.amount) 
                FROM transactions AS t 
                WHERE t.category_id = b.category_id 
                AND strftime('%Y-%m', t.date / 1000, 'unixepoch') = b.period 
                AND t.type = 'EXPENSE'
                AND t.user_uid = :userUid
            ), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        WHERE b.id = :budgetId AND b.user_uid = :userUid
    """
    )
    // DITAMBAHKAN: parameter userUid dan filter di WHERE utama dan subquery
    suspend fun getBudgetWithDetailsById(budgetId: String, userUid: String): BudgetWithDetailsDto?

    @Transaction
    @Query(
        """
        SELECT
            b.id as budgetId,
            b.amount as budgetAmount,
            b.period as period,
            c.id as categoryId,
            c.name as categoryName,
            c.icon_identifier as categoryIconIdentifier,
            COALESCE(SUM(t.amount), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        LEFT JOIN transactions AS t ON t.category_id = c.id 
                                   AND t.type = 'EXPENSE' 
                                   AND t.user_uid = :userUid
        WHERE b.period = :periodString AND b.user_uid = :userUid
        GROUP BY b.id
        ORDER BY spentAmount / b.amount DESC
        LIMIT :limit
    """
    )
    // DITAMBAHKAN: parameter userUid dan filter di WHERE dan JOIN
    fun getTopBudgetsWithDetails(
        periodString: String,
        limit: Int,
        userUid: String
    ): Flow<List<BudgetWithDetailsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :budgetId AND user_uid = :userUid")
    suspend fun deleteBudgetById(budgetId: String, userUid: String)
}