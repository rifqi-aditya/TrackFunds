package com.rifqi.trackfunds.feature.budget.ui.event

import com.rifqi.trackfunds.feature.budget.ui.model.BudgetTab
import java.time.YearMonth

sealed interface BudgetEvent {
    data class ChangePeriod(val newPeriod: YearMonth) : BudgetEvent
    data object AddBudgetClicked : BudgetEvent
    data class EditBudgetClicked(val budgetId: String) : BudgetEvent
    data class TabSelected(val tab: BudgetTab) : BudgetEvent
    data class CategoryFilterClicked(val categoryId: String) : BudgetEvent
    data object ClearCategoryFilter : BudgetEvent
    data object ChangePeriodClicked : BudgetEvent
    data class PeriodSelected(val newPeriod: YearMonth) : BudgetEvent
    data object DialogDismissed : BudgetEvent
}