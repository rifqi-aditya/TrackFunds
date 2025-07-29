package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal

interface UpdateSavingsGoalUseCase {
    suspend operator fun invoke(goal: SavingsGoal): Result<Unit>
}