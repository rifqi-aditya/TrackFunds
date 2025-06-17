package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetTransactionsByCategoryIdUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetTransactionsByCategoryIdUseCase {

    override operator fun invoke(
        categoryId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return repository.getTransactionsByCategoryId(categoryId, startDate, endDate)
    }
}