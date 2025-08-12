package com.rifqi.trackfunds.feature.transaction.ui.filter

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate

/**
 * Represents all data needed for the FilterScreen.
 */
data class FilterTransactionUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val allCategories: List<Category> = emptyList(),
    val allAccounts: List<Account> = emptyList(),

    // Menyimpan pilihan filter sementara saat pengguna memilih
    val selectedCategoryIds: Set<String> = emptySet(),
    val selectedAccountIds: Set<String> = emptySet(),
    val selectedDateRange: Pair<LocalDate?, LocalDate?> = Pair(null, null),
    val selectedDateOption: DateRangeOption = DateRangeOption.THIS_MONTH,
    val customStartDate: LocalDate? = null,
    val customEndDate: LocalDate? = null,
    val showDatePicker: Boolean = false
)