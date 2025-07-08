package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem

interface CreateSavingsGoalUseCase {
    suspend operator fun invoke(goal: SavingsGoalItem)
}