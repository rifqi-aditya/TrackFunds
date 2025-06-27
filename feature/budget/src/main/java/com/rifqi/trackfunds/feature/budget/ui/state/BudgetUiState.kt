package com.rifqi.trackfunds.feature.budget.ui.state

import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.feature.budget.ui.model.BudgetTab
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

data class BudgetUiState(
    val isLoading: Boolean = true,
    val budgets: List<BudgetItem> = emptyList(),
    val currentPeriod: YearMonth = YearMonth.now(),
    val totalBudgeted: BigDecimal = BigDecimal.ZERO,
    val totalSpent: BigDecimal = BigDecimal.ZERO,
    val error: String? = null,
    val selectedTab: BudgetTab = BudgetTab.YOUR_BUDGETS,
    val selectedCategoryFilterId: String? = null,
    val categoriesWithBudget: List<CategoryItem> = emptyList(),
    val showMonthPickerDialog: Boolean = false
) {
    val remainingOverall: BigDecimal
        get() = totalBudgeted.subtract(totalSpent)

    val currentPeriodDisplay: String
        get() = currentPeriod.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
}