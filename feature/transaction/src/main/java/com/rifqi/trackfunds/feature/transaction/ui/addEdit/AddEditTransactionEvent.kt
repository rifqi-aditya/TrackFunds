package com.rifqi.trackfunds.feature.transaction.ui.addEdit

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
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
    data class AccountSelected(val account: Account) : AddEditTransactionEvent
    data class CategorySelected(val category: Category) : AddEditTransactionEvent
    data class SavingsGoalSelected(val goal: SavingsGoal) : AddEditTransactionEvent

    // Events for dismissing selection UI
    data object DismissSheet : AddEditTransactionEvent
    data object DismissDatePicker : AddEditTransactionEvent

    // General button click events
    data object SaveClicked : AddEditTransactionEvent

    data object AddNewLineItem : AddEditTransactionEvent
    data class OnLineItemChanged(val index: Int, val item: TransactionItemInput) :
        AddEditTransactionEvent

    data class OnDeleteLineItem(val index: Int) : AddEditTransactionEvent
    data object OnAddReceiptClicked : AddEditTransactionEvent
    data class OnReceiptImageSelected(val uri: Uri) : AddEditTransactionEvent

    data class DeleteLineItem(val index: Int) : AddEditTransactionEvent

    data object ToggleLineItemsSection : AddEditTransactionEvent
    data object ToggleReceiptSection : AddEditTransactionEvent

}