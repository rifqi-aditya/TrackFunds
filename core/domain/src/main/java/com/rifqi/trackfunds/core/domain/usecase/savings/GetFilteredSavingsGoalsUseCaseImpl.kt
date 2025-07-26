package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredSavingsGoalsUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : GetFilteredSavingsGoalsUseCase {
    override operator fun invoke(filter: SavingsFilter): Flow<List<SavingsGoalModel>> {
        return repository.getFilteredGoals(filter)
    }
}