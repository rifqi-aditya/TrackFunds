package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetTransactionsByDateRangeUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
): GetTransactionsByDateRangeUseCase {
    override operator fun invoke(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return repository.getTransactionsByDateRange(startDate, endDate)
    }
}