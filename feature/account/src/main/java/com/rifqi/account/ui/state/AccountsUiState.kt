package com.rifqi.account.ui.state

import com.rifqi.trackfunds.core.domain.model.AccountModel
import java.math.BigDecimal

data class AccountsUiState(
    val isLoading: Boolean = true,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val accounts: List<AccountModel> = emptyList(),
    val error: String? = null
)