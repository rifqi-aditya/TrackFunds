package com.rifqi.account.ui.event

sealed interface AccountsEvent {
    data class AccountClicked(val accountId: String) : AccountsEvent
    data object TransferClicked : AccountsEvent
    data object AddAccountClicked : AccountsEvent
}