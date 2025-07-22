package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : UpdateTransactionUseCase {

    override suspend operator fun invoke(
        transaction: Transaction,
        oldAmount: BigDecimal,
        oldAccountId: String
    ) {
        repository.updateTransaction(
            transaction,
            oldAmount,
            oldAccountId
        )
    }
}