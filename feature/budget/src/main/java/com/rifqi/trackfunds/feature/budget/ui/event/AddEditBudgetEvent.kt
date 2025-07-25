package com.rifqi.trackfunds.feature.budget.ui.event

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import java.time.YearMonth

sealed interface AddEditBudgetEvent {
    data object SaveBudgetClicked : AddEditBudgetEvent
    data object CategorySelectorClicked : AddEditBudgetEvent
    data class AmountChanged(val amount: String) : AddEditBudgetEvent
    data class CategorySearchChanged(val query: String) : AddEditBudgetEvent

    data class CategorySelected(val category: CategoryItem) : AddEditBudgetEvent

    data object DeleteClicked : AddEditBudgetEvent
    data object ConfirmDeleteClicked : AddEditBudgetEvent
    data object DismissDeleteDialog : AddEditBudgetEvent

    data object ShowCategorySheet : AddEditBudgetEvent
    data object DismissCategorySheet : AddEditBudgetEvent

    data object PeriodSelectorClicked : AddEditBudgetEvent
    data object DismissPeriodPicker : AddEditBudgetEvent
    data class PeriodSelected(val period: YearMonth) : AddEditBudgetEvent
}