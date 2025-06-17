package com.rifqi.trackfunds.feature.categories.ui.model

import com.rifqi.trackfunds.core.domain.model.CategoryItem

data class SelectCategoryUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryItem> = emptyList(),
    val error: String? = null
)