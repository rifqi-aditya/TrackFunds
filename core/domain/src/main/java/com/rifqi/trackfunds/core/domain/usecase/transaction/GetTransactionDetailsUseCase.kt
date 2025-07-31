package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface GetTransactionDetailsUseCase {
    operator fun invoke(transactionId: String): Flow<Transaction?>
}