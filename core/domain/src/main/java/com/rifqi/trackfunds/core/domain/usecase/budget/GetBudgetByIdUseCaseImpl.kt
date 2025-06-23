package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetByIdUseCase {
    override suspend operator fun invoke(budgetId: String): BudgetItem? {
        return repository.getBudgetById(budgetId)
    }
}