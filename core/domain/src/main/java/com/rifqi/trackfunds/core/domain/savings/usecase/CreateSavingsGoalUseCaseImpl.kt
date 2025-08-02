package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateSavingsGoalUseCaseImpl @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : CreateSavingsGoalUseCase {
    override suspend operator fun invoke(goal: SavingsGoal): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(Exception("User not logged in."))

        return savingsGoalRepository.createGoal(userUid, goal)
    }
}