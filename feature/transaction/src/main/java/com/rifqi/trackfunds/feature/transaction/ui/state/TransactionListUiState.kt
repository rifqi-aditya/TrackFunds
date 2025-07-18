package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import java.math.BigDecimal

data class TransactionListUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val activeFilter: TransactionFilter = TransactionFilter(),
    val transactions: List<TransactionItem> = emptyList(),
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,
    val totalSavings: BigDecimal = BigDecimal.ZERO,
    val spendableBalance: BigDecimal = BigDecimal.ZERO,
)