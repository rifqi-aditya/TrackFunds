package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class GetCategorySummariesUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
): GetCategorySummariesUseCase {
    override operator fun invoke(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySummaryItem>> {
        return transactionRepository.getCategorySummaries(type, startDate, endDate)
    }
}