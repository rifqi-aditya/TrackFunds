package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction

interface DeleteTransactionUseCase {
    suspend operator fun invoke(transaction: Transaction)
}