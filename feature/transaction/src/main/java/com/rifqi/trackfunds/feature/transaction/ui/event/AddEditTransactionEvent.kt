package com.rifqi.trackfunds.feature.transaction.ui.event

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

sealed interface AddEditTransactionEvent {
    // Aksi dari Form Input
    data class AmountChanged(val amount: String) : AddEditTransactionEvent
    data class TransactionTypeChanged(val type: TransactionType) : AddEditTransactionEvent
    data class DescriptionChanged(val descriptions: String) : AddEditTransactionEvent
    data class DateChanged(val date: LocalDate) : AddEditTransactionEvent

    // Aksi dari Hasil Navigasi
    data class AccountSelected(val account: AccountItem) : AddEditTransactionEvent
    data class CategorySelected(val category: CategoryItem) : AddEditTransactionEvent
    data class SavingsGoalSelected(val goal: SavingsGoalItem) : AddEditTransactionEvent

    // Aksi Klik Tombol/UI
    data object SaveClicked : AddEditTransactionEvent
    data object DeleteClicked : AddEditTransactionEvent
    data object ConfirmDeleteClicked : AddEditTransactionEvent
    data object DismissDeleteDialog : AddEditTransactionEvent
    data object DateSelectorClicked : AddEditTransactionEvent
    data object CategorySelectorClicked : AddEditTransactionEvent
    data object AccountSelectorClicked : AddEditTransactionEvent
    data object SavingsGoalSelectorClicked : AddEditTransactionEvent
    data object SavingsGoalSheetDismissed : AddEditTransactionEvent
    data object ScanReceiptClicked : AddEditTransactionEvent

    // Aksi untuk mereset state
    data object ErrorMessageShown : AddEditTransactionEvent
    data object NavigationHandled : AddEditTransactionEvent // Untuk mereset sinyal navigasi
}