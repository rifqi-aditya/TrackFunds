package com.rifqi.trackfunds.feature.budget.ui.event

import com.rifqi.trackfunds.core.domain.category.model.Category
import java.time.YearMonth

/**
 * Represents events that can occur on the Add/Edit Budget screen.
 */
sealed interface AddEditBudgetEvent {
    /**
     * Event triggered when the save budget button is clicked.
     */
    data object SaveBudgetClicked : AddEditBudgetEvent

    /**
     * Event triggered when the category selector is clicked.
     */
    data object CategorySelectorClicked : AddEditBudgetEvent

    /**
     * Event triggered when the amount input changes.
     * @param amount The new amount value as a string.
     */
    data class AmountChanged(val amount: String) : AddEditBudgetEvent

    /**
     * Event triggered when the category search query changes.
     * @param query The new search query.
     */
    data class CategorySearchChanged(val query: String) : AddEditBudgetEvent

    /**
     * Event triggered when a category is selected.
     * @param category The selected category item.
     */
    data class CategorySelected(val category: Category) : AddEditBudgetEvent

    /**
     * Event triggered when the delete button is clicked.
     */
    data object DeleteClicked : AddEditBudgetEvent

    /**
     * Event triggered when the delete confirmation is clicked.
     */
    data object ConfirmDeleteClicked : AddEditBudgetEvent

    /**
     * Event triggered when the delete confirmation dialog is dismissed.
     */
    data object DismissDeleteDialog : AddEditBudgetEvent

    /**
     * Event triggered to show the category selection bottom sheet.
     */
    data object ShowCategorySheet : AddEditBudgetEvent

    /**
     * Event triggered to dismiss the category selection bottom sheet.
     */
    data object DismissCategorySheet : AddEditBudgetEvent

    /**
     * Event triggered when the period selector is clicked.
     */
    data object PeriodSelectorClicked : AddEditBudgetEvent

    /**
     * Event triggered when the period picker is dismissed.
     */
    data object DismissPeriodPicker : AddEditBudgetEvent

    /**
     * Event triggered when a period is selected.
     * @param period The selected period as a [YearMonth].
     */
    data class PeriodSelected(val period: YearMonth) : AddEditBudgetEvent
}