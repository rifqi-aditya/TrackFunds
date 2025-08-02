package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import kotlinx.coroutines.flow.Flow

interface GetTransactionDetailsUseCase {
    operator fun invoke(transactionId: String): Flow<Transaction?>
}