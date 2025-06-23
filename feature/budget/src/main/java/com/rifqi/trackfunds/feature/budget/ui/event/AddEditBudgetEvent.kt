package com.rifqi.trackfunds.feature.budget.ui.event

import com.rifqi.trackfunds.core.domain.model.CategoryItem

sealed interface AddEditBudgetEvent {
    // Aksi yang dipicu oleh UI
    data object SaveBudgetClicked : AddEditBudgetEvent
    data object CategorySelectorClicked : AddEditBudgetEvent
    data class AmountChanged(val amount: String) : AddEditBudgetEvent

    // Event internal yang dipicu setelah hasil kembali
    data class CategorySelected(val category: CategoryItem) : AddEditBudgetEvent

    data object DeleteClicked : AddEditBudgetEvent
    data object ConfirmDeleteClicked : AddEditBudgetEvent
    data object DismissDeleteDialog : AddEditBudgetEvent
}