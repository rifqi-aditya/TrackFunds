package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : DeleteSavingsGoalUseCase {
    override suspend operator fun invoke(goalId: String): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(Exception("User not logged in."))

        return repository.deleteGoal(userUid, goalId)
    }
}