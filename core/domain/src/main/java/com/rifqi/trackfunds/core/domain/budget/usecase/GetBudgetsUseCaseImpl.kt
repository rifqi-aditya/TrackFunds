package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget
import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetBudgetsUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetsUseCase {
    override operator fun invoke(period: YearMonth): Flow<List<Budget>> {
        return repository.getBudgetsForPeriod(period)
    }
}