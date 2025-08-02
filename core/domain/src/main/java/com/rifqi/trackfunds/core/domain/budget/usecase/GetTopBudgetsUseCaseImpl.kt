package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget
import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetTopBudgetsUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetTopBudgetsUseCase {
    override operator fun invoke(period: YearMonth, limit: Int): Flow<List<Budget>> {
        return repository.getTopBudgets(period, limit)
    }
}