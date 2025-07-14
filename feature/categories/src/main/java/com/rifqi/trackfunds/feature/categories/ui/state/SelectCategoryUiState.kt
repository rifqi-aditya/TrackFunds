package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryItem

data class SelectCategoryUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryItem> = emptyList(),
    val error: String? = null
)