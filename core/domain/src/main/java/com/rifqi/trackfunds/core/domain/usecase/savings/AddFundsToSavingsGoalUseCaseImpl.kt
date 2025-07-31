package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import javax.inject.Inject

class AddFundsToSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : AddFundsToSavingsGoalUseCase {
    override suspend operator fun invoke(goalId: String, amount: BigDecimal): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(Exception("User not logged in."))
        return repository.addFunds(userUid, goalId, amount)
    }
}