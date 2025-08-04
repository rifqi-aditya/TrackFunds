package com.rifqi.account.ui.list

/**
 * Merepresentasikan semua aksi yang bisa dilakukan pengguna di layar.
 */
sealed interface AccountsEvent {
    data object AddAccountClicked : AccountsEvent
    data class AccountClicked(val accountId: String) : AccountsEvent
    data class DeleteAccountConfirmed(val accountId: String) : AccountsEvent // Contoh untuk swipe-to-delete
}
