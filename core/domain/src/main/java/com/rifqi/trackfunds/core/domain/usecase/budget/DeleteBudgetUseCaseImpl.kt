package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : DeleteBudgetUseCase {
    override suspend operator fun invoke(budgetId: String): Result<Unit> {
        return repository.deleteBudget(budgetId)
    }
}