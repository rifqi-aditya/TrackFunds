package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsForSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetTransactionsForSavingsGoalUseCase {
    override operator fun invoke(goalId: String): Flow<List<TransactionItem>> {
        return repository.getTransactionsByGoalId(goalId)
    }
}