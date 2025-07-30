package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.Budget
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class UpdateBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : UpdateBudgetUseCase {
    override suspend operator fun invoke(budget: Budget): Result<Unit> {
        return repository.updateBudget(budget)
    }
}