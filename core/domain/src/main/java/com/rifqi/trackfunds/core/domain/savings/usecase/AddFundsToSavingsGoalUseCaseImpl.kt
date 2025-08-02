package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
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