package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem

interface UpdateBudgetUseCase {
    suspend operator fun invoke(budget: BudgetItem)
}