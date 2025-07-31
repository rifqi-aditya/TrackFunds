package com.rifqi.trackfunds.feature.transaction.ui.list

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import java.math.BigDecimal

data class TransactionListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeFilter: TransactionFilter = TransactionFilter(),
    val transactions: List<Transaction> = emptyList(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val totalSavings: BigDecimal = BigDecimal.ZERO,
    val netBalance: BigDecimal = BigDecimal.ZERO,
    val emptyStateTitle: String? = null,
    val emptyStateMessage: String? = null
)