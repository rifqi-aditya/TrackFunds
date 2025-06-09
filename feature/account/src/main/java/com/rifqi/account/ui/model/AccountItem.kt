package com.rifqi.account.ui.model

import java.math.BigDecimal

data class AccountSummaryItem(
    val id: String,
    val name: String,
    val balance: BigDecimal,
    val iconIdentifier: String?
)

// State untuk keseluruhan layar
data class AccountsUiState(
    val isLoading: Boolean = true,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val accounts: List<AccountSummaryItem> = emptyList(),
    val error: String? = null
)