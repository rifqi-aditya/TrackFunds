package com.rifqi.trackfunds.feature.transaction.ui.model

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import java.math.BigDecimal

data class TransactionListUiState(
    val isLoading: Boolean = true,
    val dateRange: String = "Loading...", // Contoh: "01 Jun 25 - 30 Jun 25"
    val elapsedAmount: BigDecimal = BigDecimal.ZERO,
    val upcomingAmount: BigDecimal = BigDecimal.ZERO,
    val transactions: List<TransactionItem> = emptyList(), // Gunakan langsung model domain
    val error: String? = null
)