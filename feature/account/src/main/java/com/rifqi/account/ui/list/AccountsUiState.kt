package com.rifqi.account.ui.list

/**
 * Merepresentasikan seluruh keadaan (state) untuk AccountsScreen.
 */
data class AccountsUiState(
    val isLoading: Boolean = true,
    val accounts: List<AccountItemUiModel> = emptyList(),
    val formattedTotalBalance: String = "",
    val error: String? = null,
    val emptyStateTitle: String? = null,
    val emptyStateMessage: String? = null
)

