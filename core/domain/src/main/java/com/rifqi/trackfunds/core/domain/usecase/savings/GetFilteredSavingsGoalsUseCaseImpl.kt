package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetFilteredSavingsGoalsUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetFilteredSavingsGoalsUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(filter: SavingsFilter): Flow<List<SavingsGoal>> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(emptyList())
            } else {
                repository.getFilteredGoals(userUid, filter)
            }
        }
    }
}