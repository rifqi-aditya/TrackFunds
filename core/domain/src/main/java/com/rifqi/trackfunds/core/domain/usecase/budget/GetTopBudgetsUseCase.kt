package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetModel
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

/**
 * Gets a continuous stream of the top N budgets for a specific period.
 */
interface GetTopBudgetsUseCase {
    /**
     * @param period The month and year to fetch budgets for.
     * @param limit The maximum number of budgets to return.
     * @return A [Flow] emitting the list of top budgets.
     */
    operator fun invoke(period: YearMonth, limit: Int = 3): Flow<List<BudgetModel>>
}