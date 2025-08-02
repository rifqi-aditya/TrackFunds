package com.rifqi.trackfunds.feature.home.ui.state

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter


data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val userName: String = "User",
    val currentMonth: String = YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")),

    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val totalIncome: BigDecimal = BigDecimal.ZERO,
    val totalExpense: BigDecimal = BigDecimal.ZERO,

    val totalBudgetSpent: BigDecimal = BigDecimal.ZERO,
    val totalBudgetRemaining: BigDecimal = BigDecimal.ZERO,
    val budgetProgress: Float = 0f,

    val recentExpenseTransactions: List<Transaction> = emptyList(),
    val recentIncomeTransactions: List<Transaction> = emptyList(),
    val selectedTabIndex: Int = 0,
)




