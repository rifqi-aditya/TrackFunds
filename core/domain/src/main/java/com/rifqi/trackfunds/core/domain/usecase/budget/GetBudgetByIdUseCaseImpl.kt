package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetModel
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetByIdUseCaseImpl @Inject constructor(
    private val repository: BudgetRepository
) : GetBudgetByIdUseCase {
    override suspend operator fun invoke(budgetId: String): Result<BudgetModel> {
        return repository.getBudgetById(budgetId)
    }
}
