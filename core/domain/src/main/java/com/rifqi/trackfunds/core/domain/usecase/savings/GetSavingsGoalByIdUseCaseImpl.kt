package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetSavingsGoalByIdUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetSavingsGoalByIdUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(goalId: String): Flow<SavingsGoal?> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(null)
            } else {
                repository.getGoalById(userUid, goalId)
            }
        }
    }
}