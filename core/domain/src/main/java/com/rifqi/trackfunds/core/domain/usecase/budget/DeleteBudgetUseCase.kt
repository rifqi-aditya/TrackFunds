package com.rifqi.trackfunds.core.domain.usecase.budget

interface DeleteBudgetUseCase {
    suspend operator fun invoke(budgetId: String)
}