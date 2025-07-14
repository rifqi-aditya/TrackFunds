package com.rifqi.trackfunds.feature.categories.ui.event

import com.rifqi.trackfunds.core.domain.model.TransactionType

/**
 * Represents all possible user actions on the Category List screen.
 */
sealed interface CategoryListEvent {
    data class CategoryClicked(val categoryId: String) : CategoryListEvent
    data class AddCategoryClicked(val type: TransactionType) : CategoryListEvent
}