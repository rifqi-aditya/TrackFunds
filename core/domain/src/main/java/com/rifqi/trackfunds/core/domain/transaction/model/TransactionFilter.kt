package com.rifqi.trackfunds.core.domain.transaction.model

import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate

data class TransactionFilter(
    val searchQuery: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val dateOption: DateRangeOption = DateRangeOption.THIS_MONTH,
    val accountIds: List<String>? = null,
    val categoryIds: List<String>? = null,
    val savingsGoalId: String? = null,
    val type: TransactionType? = null,
    val limit: Int? = null,
)