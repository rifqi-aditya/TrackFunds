package com.rifqi.trackfunds.feature.budget.ui.event

import java.time.YearMonth

/**
 * Represents all possible user actions that can occur on the Budget screen.
 */
sealed interface BudgetEvent {
    /**
     * Event triggered when the user clicks the button to add a new budget.
     */
    data object AddBudgetClicked : BudgetEvent

    /**
     * Event triggered when the user clicks on a specific budget item in the list.
     * @param budgetId The unique identifier of the budget item that was clicked.
     */
    data class BudgetItemClicked(val budgetId: String) : BudgetEvent

    /**
     * Event triggered when the user clicks the UI element to open the month selection dialog.
     */
    data object ChangePeriodClicked : BudgetEvent

    /**
     * Event triggered when the user selects a new month and year from the picker dialog.
     * @param newPeriod The new [YearMonth] selected by the user.
     */
    data class PeriodSelected(val newPeriod: YearMonth) : BudgetEvent

    /**
     * Event triggered when the month picker dialog is dismissed without a selection.
     */
    data object MonthPickerDialogDismissed : BudgetEvent
}