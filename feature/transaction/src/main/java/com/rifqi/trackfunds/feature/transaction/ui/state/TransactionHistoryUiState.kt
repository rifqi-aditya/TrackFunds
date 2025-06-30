package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import java.math.BigDecimal

data class TransactionHistoryUiState(
    val isLoading: Boolean = true,
    val title: String = "All Transactions",
    val dateRange: String = "Loading...", // Contoh: "01 Jun 25 - 30 Jun 25"
    val summaryAmount: BigDecimal = BigDecimal.ZERO,
    val transactions: List<TransactionItem> = emptyList(), // Gunakan langsung model domain
    val error: String? = null
)