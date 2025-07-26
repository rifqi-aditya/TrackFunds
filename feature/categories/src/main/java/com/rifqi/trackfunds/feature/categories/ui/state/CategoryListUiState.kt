package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryModel

data class CategoryListUiState(
    val isLoading: Boolean = true,
    val defaultExpenseCategories: List<CategoryModel> = emptyList(),
    val userExpenseCategories: List<CategoryModel> = emptyList(),
    val defaultIncomeCategories: List<CategoryModel> = emptyList(),
    val userIncomeCategories: List<CategoryModel> = emptyList(),
    val error: String? = null
)