package com.rifqi.trackfunds.feature.categories.ui.state

import com.rifqi.trackfunds.core.domain.category.model.TransactionType

data class AddEditCategoryUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val iconIdentifier: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val isCategorySaved: Boolean = false,
    val screenTitle: String = "Add Category",
    val error: String = "",
)