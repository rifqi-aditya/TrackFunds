package com.rifqi.trackfunds.core.domain.category.model

import java.util.UUID

enum class TransactionType {
    EXPENSE, INCOME, SAVINGS
}

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String,
    val type: TransactionType,
    val standardKey: String? = null,
    val userUid: String? = null
) {
    companion object {
        fun uncategorized(): Category {
            return Category(
                name = "Uncategorized",
                iconIdentifier = "ic_category_uncategorized",
                type = TransactionType.EXPENSE,
                standardKey = "uncategorized"
            )
        }
    }
}