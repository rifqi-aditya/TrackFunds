package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface SavingsRepository {
    fun getActiveGoals(): Flow<List<SavingsGoal>>
    suspend fun getGoalById(goalId: String): Flow<SavingsGoal?>
    suspend fun createGoal(goal: SavingsGoal)
    suspend fun addFundsToGoal(goalId: String, amount: BigDecimal)
    suspend fun copyIconToInternalStorage(imageUri: Uri): String
    suspend fun deleteGoal(goalId: String)
}