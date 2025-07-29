package com.rifqi.trackfunds.feature.savings.ui.sideeffect


/**
 * Represents the side effects that can occur in the Add/Edit Savings Goal screen.
 * Side effects are actions that are triggered by the ViewModel and observed by the UI
 * to perform actions like navigation or showing snackbars.
 */
sealed interface AddEditGoalSideEffect {

    /**
     * Represents a side effect to navigate back from the current screen.
     */
    data object NavigateBack : AddEditGoalSideEffect

    /**
     * Represents a side effect to show a snackbar with a specific message.
     * @property message The message to be displayed in the snackbar.
     */
    data class ShowSnackbar(val message: String) : AddEditGoalSideEffect
}