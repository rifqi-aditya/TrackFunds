package com.rifqi.trackfunds.feature.transaction.ui.state

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.common.model.DateRangeOption
import java.time.LocalDate

/**
 * Represents all data needed for the FilterScreen.
 */
data class FilterTransactionUiState(
    val isLoading: Boolean = true,
    val allCategories: List<CategoryItem> = emptyList(),
    val allAccounts: List<AccountItem> = emptyList(),

    // Menyimpan pilihan filter sementara saat pengguna memilih
    val selectedCategoryIds: Set<String> = emptySet(),
    val selectedAccountIds: Set<String> = emptySet(),
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val selectedDateOption: DateRangeOption = DateRangeOption.LAST_30_DAYS,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null,
    val showDatePicker: Boolean = false
)