package com.rifqi.trackfunds.feature.home.ui.state

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.utils.getCurrentDateRangePair
import com.rifqi.trackfunds.core.ui.utils.getCurrentMonthAndYear
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Represents the entire state for the HomeScreen.
 * It's a single source of truth for the UI.
 *
 * @param isLoading True if data is currently being loaded.
 * @param error An error message string if an error occurs, null otherwise.
 * @param currentMonthAndYear A formatted string for the currently selected period (e.g., "June 2025").
 * @param dateRangePeriod The start and end date of the currently selected period.
 * @param totalIncome The sum of all income for the selected period.
 * @param totalExpenses The sum of all expenses for the selected period.
 * @param incomeSummaries A list of income summaries grouped by category.
 * @param expenseSummaries A list of expense summaries grouped by category.
 * @param recentTransactions A list of the most recent transactions.
 * @param showMonthPickerDialog True if the month/year picker dialog should be shown.
 * @param isAddActionDialogVisible True if the action dialog (for Add/Scan) should be shown.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentMonthAndYear: String = getCurrentMonthAndYear(),
    val dateRangePeriod: Pair<LocalDate, LocalDate> = getCurrentDateRangePair(),

    // Data ringkasan sekarang berada di level atas
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpenses: BigDecimal = BigDecimal.ZERO,

    // Daftar-daftar data
    val incomeSummaries: List<HomeCategorySummaryItem> = emptyList(),
    val expenseSummaries: List<HomeCategorySummaryItem> = emptyList(),
    val recentTransactions: List<TransactionItem> = emptyList(),

    // Kontrol UI
    val showMonthPickerDialog: Boolean = false,
    val isAddActionDialogVisible: Boolean = false
) {
    // Properti kalkulasi untuk kemudahan
    val monthlyBalance: BigDecimal
        get() = totalIncome.subtract(totalExpenses)
}

/**
 * Represents a summarized view of transactions for a single category.
 * Used for the "Expenses by Category" and "Income by Category" sections.
 */
data class HomeCategorySummaryItem(
    val categoryId: String,
    val categoryName: String,
    val categoryIconIdentifier: String?,
    val transactionType: TransactionType,
    val totalAmount: BigDecimal,
    val budgetAmount: BigDecimal? = null // Tetap ada untuk fitur budget di Home
)




