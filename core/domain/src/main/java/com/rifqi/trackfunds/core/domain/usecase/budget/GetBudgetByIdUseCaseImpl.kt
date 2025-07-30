package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.Budget
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetByIdUseCase {
    override suspend operator fun invoke(budgetId: String): Result<Budget> {
        return repository.getBudgetById(budgetId)
    }
}
