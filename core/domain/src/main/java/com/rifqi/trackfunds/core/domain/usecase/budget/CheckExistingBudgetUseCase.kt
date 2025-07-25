package com.rifqi.trackfunds.core.domain.usecase.budget

import java.time.YearMonth

interface CheckExistingBudgetUseCase {
    suspend operator fun invoke(categoryId: String, period: YearMonth): String?
}