package com.rifqi.trackfunds.feature.categories.ui.list

import com.rifqi.trackfunds.core.domain.category.model.Category

data class CategoryListUiState(
    val isLoading: Boolean = true,
    val defaultExpenseCategories: List<Category> = emptyList(),
    val userExpenseCategories: List<Category> = emptyList(),
    val defaultIncomeCategories: List<Category> = emptyList(),
    val userIncomeCategories: List<Category> = emptyList(),
    val error: String? = null
)