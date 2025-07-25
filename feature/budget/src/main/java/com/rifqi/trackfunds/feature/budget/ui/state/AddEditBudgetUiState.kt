package com.rifqi.trackfunds.feature.budget.ui.state

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import java.math.BigDecimal
import java.time.YearMonth

data class AddEditBudgetUiState(
    val isLoading: Boolean = false,
    val selectedCategory: CategoryItem? = null,
    val amount: String = "",
    val period: YearMonth,
    val isBudgetSaved: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false,
    val initialSpentAmount: BigDecimal = BigDecimal.ZERO,

    val showCategorySheet: Boolean = false,
    val showPeriodPicker: Boolean = false,
    val categorySearchQuery: String = ""
)
