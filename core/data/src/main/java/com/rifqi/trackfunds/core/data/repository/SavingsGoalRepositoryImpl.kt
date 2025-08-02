package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.savings.model.SavingsFilter
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingsGoalRepositoryImpl @Inject constructor(
    private val savingsGoalDao: SavingsGoalDao,
) : SavingsGoalRepository {

    override fun getActiveGoals(userUid: String): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getActiveGoals(userUid)
            .map { entityList -> entityList.map { it.toDomain() } }
    }

    override fun getFilteredGoals(userUid: String, filter: SavingsFilter): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getFilteredGoals(userUid, filter.isAchieved)
            .map { entityList -> entityList.map { it.toDomain() } }
    }

    override fun getGoalById(userUid: String, goalId: String): Flow<SavingsGoal?> {
        return savingsGoalDao.getGoalById(goalId, userUid)
            .map { entity -> entity?.toDomain() }
    }

    override suspend fun createGoal(userUid: String, goal: SavingsGoal): Result<Unit> {
        return try {
            savingsGoalDao.insertGoal(goal.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGoal(userUid: String, goal: SavingsGoal): Result<Unit> {
        return try {
            val goalEntity = goal.toEntity(userUid)
            savingsGoalDao.updateGoal(goalEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFunds(
        userUid: String,
        goalId: String,
        amount: BigDecimal
    ): Result<Unit> {
        return try {
            savingsGoalDao.addFundsToGoal(goalId, amount, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGoal(userUid: String, goalId: String): Result<Unit> {
        return try {
            savingsGoalDao.deleteGoalById(goalId, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}