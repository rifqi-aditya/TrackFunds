package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.AccountModel
import java.time.LocalDate

enum class AccountSelectionMode {
    FROM, TO, NONE
}

data class TransferUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTransferSuccessful: Boolean = false,

    val fromAccount: AccountModel? = null,
    val toAccount: AccountModel? = null,
    val amount: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),

    val accountSelectionMode: AccountSelectionMode = AccountSelectionMode.NONE,
    val showDatePicker: Boolean = false
)