package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface GetTopBudgetsUseCase {
    operator fun invoke(period: YearMonth, limit: Int = 3): Flow<List<BudgetItem>>
}