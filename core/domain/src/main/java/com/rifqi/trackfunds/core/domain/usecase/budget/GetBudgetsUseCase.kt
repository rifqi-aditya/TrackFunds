package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface GetBudgetsUseCase {
    operator fun invoke(period: YearMonth): Flow<List<BudgetItem>>
}