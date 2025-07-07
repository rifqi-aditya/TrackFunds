package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import javax.inject.Inject

class DeleteSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : DeleteSavingsGoalUseCase {
    override suspend operator fun invoke(goalId: String) {
        repository.deleteGoal(goalId)
    }
}