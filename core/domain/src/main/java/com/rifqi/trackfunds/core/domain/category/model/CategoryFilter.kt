package com.rifqi.trackfunds.core.domain.category.model

data class CategoryFilter(
    val type: TransactionType? = null,
    val isUnbudgeted: Boolean? = null,
    val budgetPeriod: String? = null,
)