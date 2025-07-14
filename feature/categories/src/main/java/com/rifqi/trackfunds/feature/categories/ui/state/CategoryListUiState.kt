package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryItem

data class CategoryListUiState(
    val isLoading: Boolean = true,
    val expenseCategories: List<CategoryItem> = emptyList(),
    val incomeCategories: List<CategoryItem> = emptyList(),
    val error: String? = null
)