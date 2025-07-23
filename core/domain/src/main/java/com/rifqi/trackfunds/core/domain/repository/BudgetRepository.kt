package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

/**
 * Interface for the Budget repository.
 * Defines the contract for data operations related to user budgets.
 */
interface BudgetRepository {
    /** Fetches all budgets for a specific period for the currently logged-in user. */
    fun getBudgetsForPeriod(period: YearMonth): Flow<List<BudgetItem>>

    /** Fetches the top N budgets for a specific period for the currently logged-in user. */
    fun getTopBudgets(period: YearMonth, limit: Int): Flow<List<BudgetItem>>

    /** Fetches a single budget by its ID for the currently logged-in user. */
    suspend fun getBudgetById(budgetId: String): Result<BudgetItem>

    /** Adds a new budget for the currently logged-in user. */
    suspend fun addBudget(budgetItem: BudgetItem): Result<Unit>

    /** Updates an existing budget for the currently logged-in user. */
    suspend fun updateBudget(budget: BudgetItem): Result<Unit>

    /** Deletes a budget by its ID for the currently logged-in user. */
    suspend fun deleteBudget(budgetId: String): Result<Unit>
}