package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionModel
import java.math.BigDecimal

interface UpdateTransactionUseCase {
    suspend operator fun invoke(
        transaction: TransactionModel,
        oldAmount: BigDecimal,
        oldAccountId: String
    ): Result<Unit>
}