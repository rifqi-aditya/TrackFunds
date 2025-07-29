package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Interface for the Savings Goal repository.
 */
interface SavingsGoalRepository {
    /** Fetches a stream of active savings goals for a specific user. */
    fun getActiveGoals(userUid: String): Flow<List<SavingsGoal>>

    /** Fetches a stream of filtered savings goals for a specific user. */
    fun getFilteredGoals(userUid: String, filter: SavingsFilter): Flow<List<SavingsGoal>>

    /** Fetches a single savings goal by its ID for a specific user. */
    fun getGoalById(userUid: String, goalId: String): Flow<SavingsGoal?>

    /** Creates a new savings goal for a specific user. */
    suspend fun createGoal(userUid: String, goal: SavingsGoal): Result<Unit>

    /**
     * Updates an existing savings goal for a specific user.
     */
    suspend fun updateGoal(userUid: String, goal: SavingsGoal): Result<Unit>

    /** Adds funds to a specific savings goal for a specific user. */
    suspend fun addFunds(userUid: String, goalId: String, amount: BigDecimal): Result<Unit>

    /** Deletes a savings goal for a specific user. */
    suspend fun deleteGoal(userUid: String, goalId: String): Result<Unit>
}