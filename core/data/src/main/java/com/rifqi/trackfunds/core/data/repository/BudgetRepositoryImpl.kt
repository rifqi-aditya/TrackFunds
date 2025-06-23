package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getBudgetsForPeriod(period: YearMonth): Flow<List<BudgetItem>> {
        val periodString = period.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        return budgetDao.getBudgetsWithDetails(periodString).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override suspend fun getBudgetById(budgetId: String): BudgetItem? {
        return budgetDao.getBudgetWithDetailsById(budgetId)?.toDomain()
    }

    override suspend fun addBudget(budgetItem: BudgetItem) {
        // Kita perlu mapper dari BudgetItem ke BudgetEntity
        budgetDao.insertBudget(budgetItem.toEntity())
    }

    override suspend fun updateBudget(budget: BudgetItem) {
        // Kita butuh mapper untuk mengubah domain model kembali ke entity
        budgetDao.updateBudget(budget.toEntity())
    }

    override suspend fun deleteBudget(budgetId: String) {
        budgetDao.deleteBudgetById(budgetId)
    }

}