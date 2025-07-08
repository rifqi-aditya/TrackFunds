package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveSavingsGoalsUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : GetActiveSavingsGoalsUseCase {
    override operator fun invoke(): Flow<List<SavingsGoalItem>> {
        return repository.getActiveGoals()
    }
}