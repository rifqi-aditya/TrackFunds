package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget
import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetByIdUseCase {
    override suspend operator fun invoke(budgetId: String): Result<Budget> {
        return repository.getBudgetById(budgetId)
    }
}
