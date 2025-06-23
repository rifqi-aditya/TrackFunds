package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class UpdateBudgetUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : UpdateBudgetUseCase {
    override suspend operator fun invoke(budget: BudgetItem) {
        repository.updateBudget(budget)
    }
}