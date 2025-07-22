package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction

interface PerformTransferUseCase {
    suspend operator fun invoke(expense: Transaction, income: Transaction)
}