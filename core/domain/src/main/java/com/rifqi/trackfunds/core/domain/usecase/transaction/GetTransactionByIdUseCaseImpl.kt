package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionByIdUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : GetTransactionByIdUseCase {

    override operator fun invoke(transactionId: String): Flow<Transaction?> {
        return repository.getTransactionById(transactionId)
    }
}