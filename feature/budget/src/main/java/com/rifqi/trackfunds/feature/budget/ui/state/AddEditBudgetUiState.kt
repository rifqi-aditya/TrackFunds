package com.rifqi.trackfunds.feature.budget.ui.state

import com.rifqi.trackfunds.core.domain.category.model.Category
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Represents the UI state for the Add/Edit Budget screen.
 *
 * @property isLoading Indicates if the screen is currently loading data.
 * @property selectedCategory The currently selected category for the budget. Null if no category is selected.
 * @property amount The string representation of the budget amount entered by the user.
 * @property period The [YearMonth] for which the budget is being set.
 * @property isBudgetSaved True if the budget has been successfully saved, false otherwise.
 * @property error An error message to be displayed, if any. Null if there is no error.
 * @property showDeleteConfirmDialog True if the delete confirmation dialog should be shown, false otherwise.
 * @property initialSpentAmount The initial amount spent for this budget category and period (used when editing).
 * @property showCategorySheet True if the category selection bottom sheet should be shown, false otherwise.
 * @property showPeriodPicker True if the period picker dialog should be shown, false otherwise.
 * @property categorySearchQuery The current search query string for filtering categories.
 */
data class AddEditBudgetUiState(
    val isLoading: Boolean = false,
    val selectedCategory: Category? = null,
    val amount: String = "",
    val period: YearMonth,
    val isBudgetSaved: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false,
    /**
     *  The initial spent amount for the budget.
     *  This is used when editing an existing budget to pre-fill the spent amount.
     *  Defaults to [BigDecimal.ZERO].
     */
    val initialSpentAmount: BigDecimal = BigDecimal.ZERO,
    /**
     *  Flag to control the visibility of the category selection bottom sheet.
     *  True to show, false to hide.
     */
    val showCategorySheet: Boolean = false,
    /**
     *  Flag to control the visibility of the period picker dialog.
     *  True to show, false to hide.
     */
    val showPeriodPicker: Boolean = false,
    /**
     *  The search query used to filter categories in the category selection sheet.
     *  Empty string means no filter is applied.
     */
    val categorySearchQuery: String = ""
)
