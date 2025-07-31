package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : UpdateSavingsGoalUseCase {

    override suspend fun invoke(goal: SavingsGoal): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(IllegalStateException("User UID is null"))
        return repository.updateGoal(userUid, goal)
    }
}