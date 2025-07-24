package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

/**
 * Interface for the Savings Goal repository.
 */
interface SavingsRepository {
    /** Fetches a stream of active savings goals for the current user. */
    fun getActiveGoals(): Flow<List<SavingsGoalItem>>

    /** Fetches a stream of filtered savings goals for the current user. */
    fun getFilteredGoals(filter: SavingsFilter): Flow<List<SavingsGoalItem>>

    /** Fetches a single savings goal by its ID for the current user. */
    fun getGoalById(goalId: String): Flow<SavingsGoalItem?>

    /** Creates a new savings goal for the current user. */
    suspend fun createGoal(goal: SavingsGoalItem): Result<Unit>

    /** Adds funds to a specific savings goal for the current user. */
    suspend fun addFundsToGoal(goalId: String, amount: BigDecimal): Result<Unit>

    /** Copies a selected icon image to internal storage. */
    suspend fun copyIconToInternalStorage(imageUri: Uri): String

    /** Deletes a savings goal for the current user. */
    suspend fun deleteGoal(goalId: String): Result<Unit>
}