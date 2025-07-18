package com.rifqi.trackfunds.core.domain.model

import java.util.UUID

enum class TransactionType {
    EXPENSE, INCOME, SAVINGS
}

data class CategoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String,
    val type: TransactionType,
    val standardKey: String? = null
)