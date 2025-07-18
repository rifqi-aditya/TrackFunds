package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface SavingsGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity)

    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity)

    // Mengambil semua tujuan tabungan yang masih aktif
    @Query("SELECT * FROM savings_goals WHERE is_achieved = 0 ORDER BY target_date ASC")
    fun getActiveGoals(): Flow<List<SavingsGoalEntity>>

    @Query(
        """
        SELECT * FROM savings_goals
        WHERE (:isAchieved IS NULL OR is_achieved = :isAchieved)
        ORDER BY target_date ASC
    """
    )
    fun getFilteredGoals(isAchieved: Boolean?): Flow<List<SavingsGoalEntity>>

    @Query("SELECT * FROM savings_goals WHERE id = :goalId")
    fun getGoalById(goalId: String): Flow<SavingsGoalEntity?>

    // Menambah jumlah saldo pada tujuan tabungan tertentu
    @Query("UPDATE savings_goals SET current_amount = current_amount + :amount WHERE id = :goalId")
    suspend fun addFundsToGoal(goalId: String, amount: BigDecimal)

    @Query("DELETE FROM savings_goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: String)
}