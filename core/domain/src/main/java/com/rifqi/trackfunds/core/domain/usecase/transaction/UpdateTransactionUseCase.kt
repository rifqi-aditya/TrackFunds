package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import java.math.BigDecimal

interface UpdateTransactionUseCase {
    suspend operator fun invoke(
        transaction: TransactionItem,
        oldAmount: BigDecimal,
        oldAccountId: String
    ): Result<Unit>
}