package com.rifqi.trackfunds.feature.home.ui.state

import com.rifqi.trackfunds.core.domain.model.BudgetModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
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
    val userName: String = "User",

    val selectedTabIndex: Int = 0,

    // Data Balance Card
    val currentMonth: String = YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM")),
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val totalSavings: BigDecimal = BigDecimal.ZERO,
    val totalAccounts: Int = 0,

    val recentSavingsTransactions: List<TransactionModel> = emptyList(),
    val recentExpenseTransactions: List<TransactionModel> = emptyList(),
    val recentIncomeTransactions: List<TransactionModel> = emptyList(),

    val topBudgets: List<BudgetModel> = emptyList(),
)




