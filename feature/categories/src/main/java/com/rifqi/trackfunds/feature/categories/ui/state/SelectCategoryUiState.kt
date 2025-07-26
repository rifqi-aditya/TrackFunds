package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryModel

data class SelectCategoryUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryModel> = emptyList(),
    val error: String? = null
)