package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetActiveSavingsGoalsUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetActiveSavingsGoalsUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(): Flow<List<SavingsGoal>> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                repository.getActiveGoals(userUid)
            }
        }
    }
}