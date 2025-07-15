package com.rifqi.trackfunds.feature.transaction.ui.event

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

/**
 * Represents all user actions on the Add/Edit Transaction screen.
 */
sealed interface AddEditTransactionEvent {
    data class AmountChanged(val amount: String) : AddEditTransactionEvent
    data class TypeChanged(val type: TransactionType) : AddEditTransactionEvent
    data class DescriptionChanged(val description: String) : AddEditTransactionEvent
    data class DateSelected(val date: LocalDate) : AddEditTransactionEvent
    data class CategorySearchChanged(val query: String) : AddEditTransactionEvent

    // Events for opening selection UI (e.g., BottomSheets or Dialogs)
    data object CategorySelectorClicked : AddEditTransactionEvent
    data object AccountSelectorClicked : AddEditTransactionEvent
    data object SavingsGoalSelectorClicked : AddEditTransactionEvent
    data object DateSelectorClicked : AddEditTransactionEvent

    // Events for handling selection results
    data class AccountSelected(val account: AccountItem) : AddEditTransactionEvent
    data class CategorySelected(val category: CategoryItem) : AddEditTransactionEvent
    data class SavingsGoalSelected(val goal: SavingsGoalItem) : AddEditTransactionEvent

    // Events for dismissing selection UI
    data object DismissSheet : AddEditTransactionEvent
    data object DismissDatePicker : AddEditTransactionEvent
    data object DismissDeleteDialog : AddEditTransactionEvent

    // General button click events
    data object SaveClicked : AddEditTransactionEvent
    data object DeleteClicked : AddEditTransactionEvent
    data object ConfirmDeleteClicked : AddEditTransactionEvent
}