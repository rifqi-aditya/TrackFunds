package com.rifqi.trackfunds.feature.transaction.ui.model

import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.feature.transaction.ui.components.ToggleItem

val transactionTypes = listOf(
    ToggleItem(TransactionType.EXPENSE, "Expense"),
    ToggleItem(TransactionType.INCOME, "Income"),
    ToggleItem(TransactionType.SAVINGS, "Savings")
)