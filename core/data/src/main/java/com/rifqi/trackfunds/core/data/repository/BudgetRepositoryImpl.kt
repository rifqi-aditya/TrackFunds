package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.budget.model.Budget
import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val session: UserSessionRepository   // ← ganti dari UserPreferencesRepository
) : BudgetRepository {

    override fun getBudgetsForPeriod(period: YearMonth): Flow<List<Budget>> {
        val start = period.atDay(1)
        val end = period.atEndOfMonth().atTime(23, 59, 59)

        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                budgetDao.getBudgetsWithDetails(start, end, uid)
                    .map { list -> list.map { it.toDomain() } }
            }
        }
    }

    override fun getTopBudgets(period: YearMonth, limit: Int): Flow<List<Budget>> {
        val start = period.atDay(1)
        val end = period.atEndOfMonth().atTime(23, 59, 59)

        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                budgetDao.getTopBudgetsWithDetails(start, end, limit, uid) // ← pakai limit
                    .map { list -> list.map { it.toDomain() } }
            }
        }
    }

    override suspend fun getBudgetById(budgetId: String): Result<Budget> = try {
        val uid = session.requireActiveUserId()
        val dto = budgetDao.getBudgetWithDetailsById(budgetId, uid)
            ?: throw IllegalStateException("Budget with ID $budgetId not found")
        Result.success(dto.toDomain())
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun findBudget(categoryId: String, period: YearMonth): String? = try {
        val uid = session.requireActiveUserId()
        budgetDao.findBudgetId(uid, categoryId, period.atDay(1))
    } catch (ce: CancellationException) {
        throw ce
    } catch (_: NotAuthenticatedException) {
        null
    }

    override suspend fun addBudget(budget: Budget): Result<Unit> = try {
        val uid = session.requireActiveUserId()
        budgetDao.insertBudget(budget.toEntity(uid))
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateBudget(budget: Budget): Result<Unit> = try {
        val uid = session.requireActiveUserId()
        budgetDao.updateBudget(budget.toEntity(uid))
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteBudget(budgetId: String): Result<Unit> = try {
        val uid = session.requireActiveUserId()
        budgetDao.deleteBudgetById(budgetId, uid)
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
