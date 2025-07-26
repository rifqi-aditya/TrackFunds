package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetModel
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class AddBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : AddBudgetUseCase {
    override suspend operator fun invoke(budgetModel: BudgetModel): Result<Unit> {
        return repository.addBudget(budgetModel)
    }
}