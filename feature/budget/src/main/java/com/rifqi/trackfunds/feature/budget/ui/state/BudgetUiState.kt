package com.rifqi.trackfunds.feature.budget.ui.state

import com.rifqi.trackfunds.core.domain.model.BudgetModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Represents the entire state required to render the Budget screen.
 * This class is immutable, meaning any change creates a new object,
 * ensuring a predictable and unidirectional data flow.
 *
 * @property isLoading Indicates whether data is currently being loaded.
 * @property budgets The raw list of budget items for the current period.
 * @property currentPeriod The period (month and year) currently being displayed.
 * @property totalBudgeted The total amount budgeted across all budgets for this period.
 * @property totalSpent The total amount spent across all budgets for this period.
 * @property error The error message to display in case of a failure, or null if there is no error.
 * @property categoriesWithBudget A list of unique categories that have a budget in the current period, used for the filter options.
 * @property showMonthPickerDialog Determines whether the month and year picker dialog should be displayed.
 */
data class BudgetUiState(
    val isLoading: Boolean = true,
    val budgets: List<BudgetModel> = emptyList(),
    val currentPeriod: YearMonth = YearMonth.now(),
    val totalBudgeted: BigDecimal = BigDecimal.ZERO,
    val totalSpent: BigDecimal = BigDecimal.ZERO,
    val error: String? = null,
    val categoriesWithBudget: List<CategoryModel> = emptyList(),
    val showMonthPickerDialog: Boolean = false
) {
    /**
     * The overall remaining balance, calculated dynamically.
     * It's the result of `totalBudgeted` - `totalSpent`.
     */
    val remainingOverall: BigDecimal
        get() = totalBudgeted.subtract(totalSpent)

    /**
     * A string representation of the current period for display in the UI.
     * Example: "July 2025".
     */
    val currentPeriodDisplay: String
        get() = currentPeriod.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
}