package com.rifqi.trackfunds.core.domain.model.filter

import com.rifqi.trackfunds.core.domain.model.TransactionType

data class CategoryFilter(
    val type: TransactionType? = null,
    val isUnbudgeted: Boolean? = null,
    val budgetPeriod: String? = null,
)