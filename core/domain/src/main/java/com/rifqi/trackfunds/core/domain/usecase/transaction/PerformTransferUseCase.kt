package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.AccountItem
import java.math.BigDecimal
import java.time.LocalDateTime

interface PerformTransferUseCase {
    suspend operator fun invoke(
        fromAccount: AccountItem,
        toAccount: AccountItem,
        amount: BigDecimal,
        date: LocalDateTime,
        note: String
    )
}