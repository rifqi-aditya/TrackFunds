package com.rifqi.account.ui.state

import com.rifqi.trackfunds.core.domain.model.Account
import java.math.BigDecimal

data class AccountsUiState(
    val isLoading: Boolean = true,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val accounts: List<Account> = emptyList(),
    val error: String? = null
)