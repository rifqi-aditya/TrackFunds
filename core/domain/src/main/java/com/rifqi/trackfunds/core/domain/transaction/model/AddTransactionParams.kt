package com.rifqi.trackfunds.core.domain.transaction.model

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import java.math.BigDecimal
import java.time.LocalDate

data class AddTransactionParams(
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDate,
    val receiptImageUri: String? = null,
    val account: Account,
    val category: Category?,
    val savingsGoal: SavingsGoal?,
    val items: List<TransactionItem>
)