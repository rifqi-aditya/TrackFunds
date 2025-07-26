package com.rifqi.trackfunds.feature.transaction.ui.event

import com.rifqi.trackfunds.core.domain.model.AccountModel
import java.time.LocalDate

sealed interface TransferEvent {
    data class FromAccountSelected(val account: AccountModel) : TransferEvent
    data class ToAccountSelected(val account: AccountModel) : TransferEvent
    data class AmountChanged(val amount: String) : TransferEvent
    data class descriptionChanged(val description: String) : TransferEvent
    data class DateChanged(val date: LocalDate) : TransferEvent
    data object SelectFromAccountClicked : TransferEvent
    data object SelectToAccountClicked : TransferEvent
    data object DatePickerDismissed : TransferEvent
    data object PerformTransferClicked : TransferEvent
    data object DateSelectorClicked : TransferEvent
}