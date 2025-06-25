package com.rifqi.trackfunds.feature.home.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRangePair
import com.rifqi.trackfunds.core.ui.util.getCurrentMonthAndYear
import java.math.BigDecimal
import java.time.LocalDate

data class HomeUiState(
    val isLoading: Boolean = true,
    val currentMonthAndYear: String = getCurrentMonthAndYear(),
    val dateRangePeriod: Pair<LocalDate, LocalDate> = getCurrentDateRangePair(),
    val summary: HomeSummary? = null,
    val challengeMessage: String? = null,
    val error: String? = null,
    val showMonthPickerDialog: Boolean = false,
    val isAddActionDialogVisible: Boolean = false,
)

data class HomeSummary(
    val monthlyBalance: BigDecimal,
    val totalExpenses: BigDecimal,
    val totalIncome: BigDecimal,
    val expenseSummaries: List<HomeCategorySummaryItem>,
    val incomeSummaries: List<HomeCategorySummaryItem>
)

data class HomeCategorySummaryItem(
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val transactionType: TransactionType,
    val totalAmount: BigDecimal,
)




