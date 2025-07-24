package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SavingsRepositoryImpl @Inject constructor(
    private val savingsGoalDao: SavingsGoalDao,
    private val userPreferencesRepository: UserPreferencesRepository, // BARU
    @ApplicationContext private val context: Context
) : SavingsRepository {


    override fun getActiveGoals(): Flow<List<SavingsGoalItem>> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList()) else savingsGoalDao.getActiveGoals(uid)
                .map { entityList -> entityList.map { it.toDomain() } }
        }
    }

    override fun getFilteredGoals(filter: SavingsFilter): Flow<List<SavingsGoalItem>> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList()) else savingsGoalDao.getFilteredGoals(
                uid,
                filter.isAchieved
            )
                .map { entityList -> entityList.map { it.toDomain() } }
        }
    }

    override fun getGoalById(goalId: String): Flow<SavingsGoalItem?> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { uid ->
            if (uid == null) flowOf(null) else savingsGoalDao.getGoalById(goalId, uid)
                .map { entity -> entity?.toDomain() }
        }
    }

    override suspend fun createGoal(goal: SavingsGoalItem): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))
            savingsGoalDao.insertGoal(goal.toEntity(uid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFundsToGoal(goalId: String, amount: BigDecimal): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))
            savingsGoalDao.addFundsToGoal(goalId, amount, uid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun copyIconToInternalStorage(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val newFileName = "savings_icon_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, newFileName)
        inputStream?.use { input -> file.outputStream().use { output -> input.copyTo(output) } }
        return file.absolutePath
    }

    override suspend fun deleteGoal(goalId: String): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))
            savingsGoalDao.deleteGoalById(goalId, uid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}