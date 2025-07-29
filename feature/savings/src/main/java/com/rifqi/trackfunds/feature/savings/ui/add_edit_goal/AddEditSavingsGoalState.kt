package com.rifqi.trackfunds.feature.savings.ui.add_edit_goal

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Represents the state of the Add/Edit Savings Goal screen.
 *
 * This data class holds all the necessary information to render the UI and manage user input
 * when creating or modifying a savings goal.
 *
 * @property isLoading Indicates whether a background operation (e.g., saving data) is in progress.
 * @property error A general error message to be displayed to the user, if any.
 * @property showIconPicker Controls the visibility of the icon picker dialog.
 * @property showDatePicker Controls the visibility of the date picker dialog.
 * @property goalName The current value of the savings goal name input field.
 * @property goalNameError An error message associated with the goal name input, if any.
 * @property targetAmount The current value of the target amount input field (as a String).
 * @property targetAmountError An error message associated with the target amount input, if any.
 * @property targetDate The selected target date for the savings goal.
 * @property targetDateError An error message associated with the target date selection, if any.
 * @property iconIdentifier The identifier for the selected icon for the savings goal.
 * @property iconError An error message associated with the icon selection, if any.
 */
data class AddEditSavingsGoalState(
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val pageTitle: String = "Add Savings Goal",
    val editingGoalId: String? = null,
    val error: String? = null,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val originalSavedAmount: BigDecimal = BigDecimal.ZERO,

    // Input field states
    val goalName: String = "",
    val goalNameError: String? = null,

    val targetAmount: String = "",
    val targetAmountError: String? = null,

    val targetDate: LocalDate? = null,
    val targetDateError: String? = null,

    val iconIdentifier: String = "",
    val iconError: String? = null,
)