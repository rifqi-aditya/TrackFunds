package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryItem

data class CategoryListUiState(
    val isLoading: Boolean = true,
    val defaultExpenseCategories: List<CategoryItem> = emptyList(),
    val userExpenseCategories: List<CategoryItem> = emptyList(),
    val defaultIncomeCategories: List<CategoryItem> = emptyList(),
    val userIncomeCategories: List<CategoryItem> = emptyList(),
    val error: String? = null
)