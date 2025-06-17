package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetTransactionsByTypeUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetTransactionsByTypeUseCase {

    override operator fun invoke(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return repository.getTransactionsByType(type, startDate, endDate)
    }
}