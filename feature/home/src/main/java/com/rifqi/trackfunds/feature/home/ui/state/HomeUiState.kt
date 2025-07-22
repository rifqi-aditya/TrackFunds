package com.rifqi.trackfunds.feature.home.ui.state

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * Represents the entire state for the HomeScreen.
 * It's a single source of truth for the UI.
 *
 * @param isLoading True if data is currently being loaded.
 * @param error An error message string if an error occurs, null otherwise.
 * @param currentMonth A formatted string for the currently month's.
 * @param userName The name of the current user.
 * @param totalExpenseThisMonth The sum of all expenses for the current month.
 * @param totalBalance The sum of all balances from all accounts.
 * @param totalSavings The sum of all current amounts from all savings goals.
 * @param totalAccounts The total number of user accounts.
 * @param recentTransactions A list of the most recent transactions.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,

    // Data Header
    val userName: String = "Rifqi Aditya",

    // Data Balance Card
    val currentMonth: String = YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM")),
    val totalExpenseThisMonth: BigDecimal = BigDecimal.ZERO,
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val totalSavings: BigDecimal = BigDecimal.ZERO,
    val totalAccounts: Int = 0,

    val recentTransactions: List<Transaction> = emptyList(),
    val topBudgets: List<BudgetItem> = emptyList(),
)

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




