package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
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