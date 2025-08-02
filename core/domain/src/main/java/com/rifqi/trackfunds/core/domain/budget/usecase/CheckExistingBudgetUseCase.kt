package com.rifqi.trackfunds.core.domain.budget.usecase

import java.time.YearMonth

interface CheckExistingBudgetUseCase {
    suspend operator fun invoke(categoryId: String, period: YearMonth): String?
}