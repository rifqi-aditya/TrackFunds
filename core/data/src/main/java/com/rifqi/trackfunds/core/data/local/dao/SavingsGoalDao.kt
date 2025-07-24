package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.entity.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Data Access Object for the savings_goals table.
 */
@Dao
interface SavingsGoalDao {
    /**
     * Inserts a single savings goal.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity)

    /**
     * Updates an existing savings goal.
     */
    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity)

    /**
     * Fetches all active savings goals for a specific user.
     */
    @Query("SELECT * FROM savings_goals WHERE is_achieved = 0 AND user_uid = :userUid ORDER BY target_date ASC")
    fun getActiveGoals(userUid: String): Flow<List<SavingsGoalEntity>>

    /**
     * Fetches a filtered list of savings goals for a specific user.
     */
    @Query(
        """
        SELECT * FROM savings_goals
        WHERE (:isAchieved IS NULL OR is_achieved = :isAchieved) AND user_uid = :userUid
        ORDER BY target_date ASC
    """
    )
    fun getFilteredGoals(userUid: String, isAchieved: Boolean?): Flow<List<SavingsGoalEntity>>

    /**
     * Fetches a single savings goal by its ID for a specific user.
     */
    @Query("SELECT * FROM savings_goals WHERE id = :goalId AND user_uid = :userUid")
    fun getGoalById(goalId: String, userUid: String): Flow<SavingsGoalEntity?>

    /**
     * Adds funds to a specific savings goal for a specific user.
     */
    @Query("UPDATE savings_goals SET current_amount = current_amount + :amount WHERE id = :goalId AND user_uid = :userUid")
    suspend fun addFundsToGoal(goalId: String, amount: BigDecimal, userUid: String)

    /**
     * Deletes a savings goal by its ID for a specific user.
     */
    @Query("DELETE FROM savings_goals WHERE id = :goalId AND user_uid = :userUid")
    suspend fun deleteGoalById(goalId: String, userUid: String)
}