package com.rifqi.account.ui.list

/**
 * Merepresentasikan perintah sekali jalan dari ViewModel ke UI.
 */
sealed interface AccountsSideEffect {
    data object NavigateToAddAccount : AccountsSideEffect
    data class NavigateToEditAccount(val accountId: String) : AccountsSideEffect
    data class ShowSnackbar(val message: String) : AccountsSideEffect
}