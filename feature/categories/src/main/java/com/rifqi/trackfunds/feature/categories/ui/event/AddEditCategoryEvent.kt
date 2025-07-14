package com.rifqi.trackfunds.feature.categories.ui.event

sealed interface AddEditCategoryEvent {
    data class NameChanged(val name: String) : AddEditCategoryEvent
    data class IconChanged(val iconIdentifier: String) : AddEditCategoryEvent
    data object SaveClicked : AddEditCategoryEvent
}