package com.rifqi.account.ui.model

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import java.math.BigDecimal
import java.time.LocalDate

data class AccountTimelineUiState(
    val isLoading: Boolean = true,
    val accountName: String = "Loading...",
    val currentBalance: BigDecimal = BigDecimal.ZERO,
    // Mengelompokkan transaksi berdasarkan tanggal
    val groupedTransactions: Map<LocalDate, List<TransactionItem>> = emptyMap(),
    val error: String? = null
)
