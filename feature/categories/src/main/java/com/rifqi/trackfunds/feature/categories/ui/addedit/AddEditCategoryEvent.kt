package com.rifqi.trackfunds.feature.categories.ui.addedit

import com.rifqi.trackfunds.core.domain.category.model.TransactionType

sealed interface AddEditCategoryEvent {
    data class NameChanged(val name: String) : AddEditCategoryEvent
    data class IconChanged(val iconIdentifier: String) : AddEditCategoryEvent
    data class TypeChanged(val type: TransactionType) : AddEditCategoryEvent

    data object IconPickerOpened : AddEditCategoryEvent
    data object IconPickerDismissed : AddEditCategoryEvent

    data object SaveClicked : AddEditCategoryEvent
    data object DeleteClicked : AddEditCategoryEvent
}