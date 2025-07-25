package com.rifqi.trackfunds.feature.budget.ui.sideeffect

import java.time.YearMonth

/**
 * Represents side effects for the Add/Edit Budget feature.
 * These side effects are used to trigger navigation or other UI-related actions.
 */
sealed interface AddEditBudgetSideEffect {
    data object NavigateBack : AddEditBudgetSideEffect

    data class NavigateToEditMode(
        val budgetId: String,
        val period: YearMonth
    ) : AddEditBudgetSideEffect
}