package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val userPreferencesRepository: UserPreferencesRepository // Inject UserPreferencesRepository
) : BudgetRepository {

    override fun getBudgetsForPeriod(period: YearMonth): Flow<List<BudgetItem>> {
        val periodString = period.format(DateTimeFormatter.ofPattern("yyyy-MM"))

        // Dibuat reaktif terhadap perubahan user
        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                // Panggil DAO dengan userUid
                budgetDao.getBudgetsWithDetails(periodString, userUid).map { dtoList ->
                    dtoList.map { it.toDomain() }
                }
            }
        }
    }

    override fun getTopBudgets(period: YearMonth, limit: Int): Flow<List<BudgetItem>> {
        val periodString = period.format(DateTimeFormatter.ofPattern("yyyy-MM"))

        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                budgetDao.getTopBudgetsWithDetails(periodString, limit, userUid).map { entities ->
                    entities.map { it.toDomain() }
                }
            }
        }
    }

    override suspend fun getBudgetById(budgetId: String): Result<BudgetItem> {
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

    override suspend fun addBudget(budgetItem: BudgetItem): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            budgetDao.insertBudget(budgetItem.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBudget(budget: BudgetItem): Result<Unit> {
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