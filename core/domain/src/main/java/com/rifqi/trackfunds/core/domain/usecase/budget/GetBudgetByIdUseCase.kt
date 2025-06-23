package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem

interface GetBudgetByIdUseCase {
    suspend operator fun invoke(budgetId: String): BudgetItem?
}