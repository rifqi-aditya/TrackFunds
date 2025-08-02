package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

/**
 * Gets a continuous stream of all budgets for a specific period for the current user.
 */
interface GetBudgetsUseCase {
    /**
     * @param period The month and year to fetch budgets for.
     * @return A [Flow] emitting the list of budgets.
     */
    operator fun invoke(period: YearMonth): Flow<List<Budget>>
}