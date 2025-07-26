package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import javax.inject.Inject

class CreateSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : CreateSavingsGoalUseCase {
    override suspend operator fun invoke(goal: SavingsGoalModel): Result<Unit> {
        return repository.createGoal(goal)
    }
}