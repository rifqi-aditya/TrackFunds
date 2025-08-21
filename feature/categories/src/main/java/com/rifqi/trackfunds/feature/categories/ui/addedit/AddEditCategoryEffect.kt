package com.rifqi.trackfunds.feature.categories.ui.addedit

sealed interface AddEditCategoryEffect {
    data object Saved : AddEditCategoryEffect
    data object Deleted : AddEditCategoryEffect
    data class ShowMessage(val message: String) : AddEditCategoryEffect
}