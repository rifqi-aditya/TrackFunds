package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.Budget
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : BudgetRepository {

    override fun getBudgetsForPeriod(period: YearMonth): Flow<List<Budget>> {
        val startOfMonth = period.atDay(1)
        val endOfMonth = period.atEndOfMonth().atTime(23, 59, 59)

        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                budgetDao.getBudgetsWithDetails(startOfMonth, endOfMonth, userUid).map { dtoList ->
                    dtoList.map { it.toDomain() }
                }
            }
        }
    }

    override fun getTopBudgets(period: YearMonth, limit: Int): Flow<List<Budget>> {
        val startOfMonth = period.atDay(1)
        val endOfMonth = period.atEndOfMonth().atTime(23, 59, 59)
        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                budgetDao.getTopBudgetsWithDetails(startOfMonth, endOfMonth, 3, userUid)
                    .map { entities ->
                        entities.map { it.toDomain() }
                    }
            }
        }
    }

    override suspend fun getBudgetById(budgetId: String): Result<Budget> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            val budget = budgetDao.getBudgetWithDetailsById(budgetId, userUid)?.toDomain()
                ?: return Result.failure(Exception("Budget not found."))

            Result.success(budget)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun findBudget(categoryId: String, period: YearMonth): String? {
        val userUid = userPreferencesRepository.userUidFlow.first() ?: return null
        val periodDate = period.atDay(1)
        return budgetDao.findBudgetId(userUid, categoryId, periodDate)
    }

    override suspend fun addBudget(budget: Budget): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            budgetDao.insertBudget(budget.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBudget(budget: Budget): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            budgetDao.updateBudget(budget.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBudget(budgetId: String): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            budgetDao.deleteBudgetById(budgetId, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}