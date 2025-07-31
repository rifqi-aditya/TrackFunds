package com.rifqi.trackfunds.core.domain.model.params

import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate

data class UpdateTransactionParams(
    val id: String,
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType,
    val date: LocalDate,
    val account: Account,
    val category: Category?,
    val savingsGoal: SavingsGoal?,
    val items: List<TransactionItem>
)