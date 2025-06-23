package com.rifqi.trackfunds.feature.budget.ui.event

import java.time.YearMonth

sealed interface BudgetListEvent {
    data class ChangePeriod(val newPeriod: YearMonth) : BudgetListEvent
    data object AddBudgetClicked : BudgetListEvent
    data class EditBudgetClicked(val budgetId: String) : BudgetListEvent
}