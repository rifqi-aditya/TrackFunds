package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem

interface AddBudgetUseCase {
    suspend operator fun invoke(budgetItem: BudgetItem)
}