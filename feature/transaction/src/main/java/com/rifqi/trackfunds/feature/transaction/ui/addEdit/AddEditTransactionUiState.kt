package com.rifqi.trackfunds.feature.transaction.ui.addEdit

import android.net.Uri
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * Enum to represent which bottom sheet is currently active.
 * This ensures only one sheet can be open at a time.
 */
enum class AddEditSheetType {
    CATEGORY, ACCOUNT, SAVINGS_GOAL
}

/**
 * Represents the entire state for the Add/Edit Transaction screen.
 * It's a single source of truth for the UI.
 */
data class AddEditTransactionUiState(
    // --- General States ---
    /** True if initial data is being loaded (in edit mode). */
    val isLoading: Boolean = false,
    /** True if the save/update operation is in progress. */
    val isSaving: Boolean = false,
    /** An error message string if a validation or operation error occurs, null otherwise. */
    val error: String? = null,

    // --- Form Field States ---
    val amount: String = "",
    val amountError: String? = null,
    /** The description of the transaction, can be empty. */
    val description: String = "",
    /** The date of the transaction, defaults to today. */
    val selectedDate: LocalDate = LocalDate.now(),
    val dateError: String? = null,
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE,
    val selectedAccount: Account? = null,
    val accountError: String? = null,
    val selectedCategory: Category? = null,
    val categoryError: String? = null,
    val selectedSavingsGoal: SavingsGoal? = null,

    // --- UI Control States ---
    /** Determines which bottom sheet is currently shown, if any. */
    val activeSheet: AddEditSheetType? = null,
    /** True if the date picker dialog should be shown. */
    val showDatePicker: Boolean = false,

    // --- Data for Selection Sheets ---
    /** The list of all accounts to choose from. */
    val allAccounts: List<Account> = emptyList(),
    /** The list of all savings goals to choose from. */
    val allSavingsGoals: List<SavingsGoal> = emptyList(),
    /** The current search query in the category picker. */
    val categorySearchQuery: String = "",

    val items: List<TransactionItemInput> = emptyList(),
    val receiptImageUri: Uri? = null,
    val isItemsExpanded: Boolean = false,
    val isReceiptExpanded: Boolean = false
) {
    val amountAsBigDecimal: BigDecimal
        get() = amount.replace(".", "").toBigDecimalOrNull() ?: BigDecimal.ZERO
}

data class TransactionItemInput(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: String = "",
    val price: String = "",
    val nameError: String? = null,
    val quantityError: String? = null,
    val priceError: String? = null
) {
    val priceAsBigDecimal: BigDecimal
        get() = price.replace(".", "").toBigDecimalOrNull() ?: BigDecimal.ZERO
}