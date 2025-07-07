package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import kotlinx.coroutines.flow.Flow

interface GetTransactionsForSavingsGoalUseCase {
    operator fun invoke(goalId: String): Flow<List<TransactionItem>>
}
