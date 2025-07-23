package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.math.BigDecimal
import javax.inject.Inject

class SavingsRepositoryImpl @Inject constructor(
    private val savingsGoalDao: SavingsGoalDao,
    @ApplicationContext private val context: Context
) : SavingsRepository {

    override fun getActiveGoals(): Flow<List<SavingsGoalItem>> {
        return savingsGoalDao.getActiveGoals().map { entityList ->
            entityList.map { it.toDomain() }
        }
    }

    override fun getFilteredGoals(filter: SavingsFilter): Flow<List<SavingsGoalItem>> {
        return savingsGoalDao.getFilteredGoals(isAchieved = filter.isAchieved)
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }

    override suspend fun getGoalById(goalId: String): Flow<SavingsGoalItem?> {
        return savingsGoalDao.getGoalById(goalId)
            .map { entity ->
                entity?.toDomain()
            }
    }

    override suspend fun createGoal(goal: SavingsGoalItem) {
        savingsGoalDao.insertGoal(goal.toEntity())
    }

    override suspend fun addFundsToGoal(goalId: String, amount: BigDecimal): Result<Unit> {
        return try {
            savingsGoalDao.addFundsToGoal(goalId, amount)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun copyIconToInternalStorage(imageUri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        // Buat nama file unik berdasarkan waktu
        val newFileName = "savings_icon_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, newFileName)

        // Salin data dari galeri ke file internal
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        // Kembalikan path absolut dari file yang baru dibuat
        return file.absolutePath
    }

    override suspend fun deleteGoal(goalId: String) {
        savingsGoalDao.deleteGoalById(goalId)
    }
}