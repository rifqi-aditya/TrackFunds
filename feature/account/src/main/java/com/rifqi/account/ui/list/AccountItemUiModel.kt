package com.rifqi.account.ui.list

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.ui.utils.formatCurrency

/**
 * Merepresentasikan data untuk satu baris akun di UI.
 * Berisi data yang sudah diformat dan siap ditampilkan.
 */
data class AccountItemUiModel(
    val id: String,
    val name: String,
    val iconIdentifier: String,
    val formattedBalance: String
)

fun Account.toUiModel(): AccountItemUiModel {
    return AccountItemUiModel(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        formattedBalance = formatCurrency(this.balance)
    )
}