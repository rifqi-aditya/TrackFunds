package com.rifqi.trackfunds.feature.transaction.ui.filter

import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate

data class FilterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val allCategories: List<CategoryData> = emptyList(),
    val allAccounts: List<AccountData> = emptyList(),

    val selectedCategoryIds: Set<String> = emptySet(),
    val selectedAccountIds: Set<String> = emptySet(),
    val selectedTypes: Set<TransactionType> = emptySet(),

    val dateOption: DateRangeOption = DateRangeOption.THIS_MONTH,
    val customStart: LocalDate? = null,
    val customEnd: LocalDate? = null,

    // UI-only
    val showAllCategories: Boolean = false,
    val initialVisibleCategoryCount: Int = 6
)