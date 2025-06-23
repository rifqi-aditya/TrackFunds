package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetBudgetsUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetsUseCase {

    override operator fun invoke(period: YearMonth): Flow<List<BudgetItem>> {
        return repository.getBudgetsForPeriod(period)
    }
}