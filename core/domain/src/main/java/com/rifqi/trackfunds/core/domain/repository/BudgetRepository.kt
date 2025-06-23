package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface BudgetRepository {
    fun getBudgetsForPeriod(period: YearMonth): Flow<List<BudgetItem>>

    suspend fun getBudgetById(budgetId: String): BudgetItem?

    suspend fun addBudget(budgetItem: BudgetItem)

    suspend fun updateBudget(budget: BudgetItem)

    suspend fun deleteBudget(budgetId: String)
}