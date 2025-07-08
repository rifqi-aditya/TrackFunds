package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface SavingsRepository {
    fun getActiveGoals(): Flow<List<SavingsGoalItem>>
    suspend fun getGoalById(goalId: String): Flow<SavingsGoalItem?>
    suspend fun createGoal(goal: SavingsGoalItem)
    suspend fun addFundsToGoal(goalId: String, amount: BigDecimal)
    suspend fun copyIconToInternalStorage(imageUri: Uri): String
    suspend fun deleteGoal(goalId: String)
}