package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import java.time.YearMonth
import javax.inject.Inject

class CheckExistingBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : CheckExistingBudgetUseCase {
    override suspend operator fun invoke(categoryId: String, period: YearMonth): String? {
        return repository.findBudget(categoryId, period)
    }
}