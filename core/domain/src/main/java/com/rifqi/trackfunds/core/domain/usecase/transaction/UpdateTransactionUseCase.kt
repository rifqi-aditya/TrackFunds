package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import java.math.BigDecimal

interface UpdateTransactionUseCase {
    suspend operator fun invoke(
        transaction: Transaction,
        oldAmount: BigDecimal,
        oldAccountId: String
    ): Result<Unit>
}