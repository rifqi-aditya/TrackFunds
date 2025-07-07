package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import javax.inject.Inject

class CreateSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : CreateSavingsGoalUseCase {
    override suspend operator fun invoke(goal: SavingsGoal) {
        repository.createGoal(goal)
    }
}