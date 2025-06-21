package com.rifqi.trackfunds.feature.transaction.ui.event

import com.rifqi.trackfunds.core.domain.model.AccountItem
import java.time.LocalDate

sealed interface TransferEvent {
    data class FromAccountSelected(val account: AccountItem) : TransferEvent
    data class ToAccountSelected(val account: AccountItem) : TransferEvent
    data class AmountChanged(val amount: String) : TransferEvent
    data class NoteChanged(val note: String) : TransferEvent
    data class DateChanged(val date: LocalDate) : TransferEvent
    data object SelectFromAccountClicked : TransferEvent
    data object SelectToAccountClicked : TransferEvent
    data object DatePickerDismissed : TransferEvent
    data object PerformTransferClicked : TransferEvent
    data object DateSelectorClicked : TransferEvent
}