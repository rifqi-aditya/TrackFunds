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
            -- Hitung total transaksi pengeluaran untuk kategori ini di rentang waktu yang sesuai
            -- COALESCE digunakan untuk menghasilkan 0 jika tidak ada transaksi sama sekali
            COALESCE(SUM(t.amount), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        -- LEFT JOIN penting agar budget dengan 0 transaksi tetap muncul
        LEFT JOIN transactions AS t ON t.category_id = c.id 
                                    AND t.type = 'EXPENSE' 
                                    AND strftime('%Y-%m', t.date / 1000, 'unixepoch') = b.period
        WHERE b.period = :period
        GROUP BY b.id, c.id
    """
    )
    fun getBudgetsWithDetails(period: String): Flow<List<BudgetWithDetailsDto>>

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
            ), 0) as spentAmount
        FROM budgets AS b
        INNER JOIN categories AS c ON b.category_id = c.id
        WHERE b.id = :budgetId
    """
    )
    suspend fun getBudgetWithDetailsById(budgetId: String): BudgetWithDetailsDto?

    @Transaction
    @Query("""
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
                                    AND b.period = :periodString
        WHERE b.period = :periodString
        GROUP BY b.id
        ORDER BY b.amount DESC
        LIMIT :limit
    """)
    fun getTopBudgetsWithDetails(periodString: String, limit: Int): Flow<List<BudgetWithDetailsDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudgetById(budgetId: String)
}