package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsByTypeUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetTransactionsByTypeUseCase {
    override operator fun invoke(type: TransactionType): Flow<List<TransactionItem>> {
        return transactionRepository.getTransactionsByType(type)
    }
}