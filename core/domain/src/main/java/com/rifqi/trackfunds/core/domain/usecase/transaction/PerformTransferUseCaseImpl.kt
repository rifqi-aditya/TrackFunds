package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class PerformTransferUseCaseImpl @Inject constructor(
    private val repository: TransactionRepository
) : PerformTransferUseCase {

    override suspend operator fun invoke(
        fromAccount: AccountItem,
        toAccount: AccountItem,
        amount: BigDecimal,
        date: LocalDateTime,
        note: String
    ) {
        val transferId = UUID.randomUUID().toString()

        val expenseTransaction = TransactionItem(
            id = UUID.randomUUID().toString(),
            note = "Transfer to ${toAccount.name}${if (note.isNotBlank()) ": $note" else ""}",
            amount = amount,
            type = TransactionType.EXPENSE,
            date = date,
            accountId = fromAccount.id,
            accountName = fromAccount.name,
            categoryId = null,
            categoryName = "Transfer",
            iconIdentifier = "ic_transfer",
            transferPairId = transferId
        )

        val incomeTransaction = TransactionItem(
            id = UUID.randomUUID().toString(),
            note = "Transfer from ${fromAccount.name}${if (note.isNotBlank()) ": $note" else ""}",
            amount = amount,
            type = TransactionType.INCOME,
            date = date,
            accountId = toAccount.id,
            accountName = toAccount.name,
            categoryId = null,
            categoryName = "Transfer",
            iconIdentifier = "ic_transfer",
            transferPairId = transferId
        )

        repository.performTransfer(expenseTransaction, incomeTransaction)
    }
}