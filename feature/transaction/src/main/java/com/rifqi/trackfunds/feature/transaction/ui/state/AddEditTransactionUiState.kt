package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

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
    val description: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE,
    val selectedAccount: AccountItem? = null,
    val selectedCategory: CategoryItem? = null,
    val selectedSavingsGoal: SavingsGoalItem? = null,

    // --- UI Control States ---
    /** Determines which bottom sheet is currently shown, if any. */
    val activeSheet: AddEditSheetType? = null,
    /** True if the date picker dialog should be shown. */
    val showDatePicker: Boolean = false,
    /** True if the delete confirmation dialog should be shown. */
    val showDeleteConfirmDialog: Boolean = false,

    // --- Data for Selection Sheets ---
    /** The list of all accounts to choose from. */
    val allAccounts: List<AccountItem> = emptyList(),
    /** The list of all categories (for the selected type) to choose from. */
    val allCategories: List<CategoryItem> = emptyList(),
    /** The list of all savings goals to choose from. */
    val allSavingsGoals: List<SavingsGoalItem> = emptyList(),
    /** The current search query in the category picker. */
    val categorySearchQuery: String = ""
)