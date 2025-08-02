package com.rifqi.trackfunds.feature.transaction.ui.model

import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.feature.transaction.ui.components.ToggleItem

val TransactionTypes = listOf(
    ToggleItem(TransactionType.EXPENSE, "Expense"),
    ToggleItem(TransactionType.INCOME, "Income"),
//    ToggleItem(TransactionType.SAVINGS, "Savings")
)