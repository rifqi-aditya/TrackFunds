package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class AddBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : AddBudgetUseCase {
    override suspend operator fun invoke(budgetItem: BudgetItem) {
        repository.addBudget(budgetItem)
    }
}