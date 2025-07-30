package com.rifqi.trackfunds.feature.budget.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.Budget
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetRoutes
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadBudgets()
    }

    /**
     * The entry point for all UI events.
     * It acts as a dispatcher, delegating tasks to the appropriate handler functions.
     */
    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.PeriodSelected -> updatePeriodAndReload(event.newPeriod)
            is BudgetEvent.BudgetItemClicked -> navigateToBudgetScreen(event.budgetId)
            BudgetEvent.AddBudgetClicked -> navigateToBudgetScreen()
            BudgetEvent.ChangePeriodClicked -> showMonthPickerDialog(true)
            BudgetEvent.MonthPickerDialogDismissed -> showMonthPickerDialog(false)
        }
    }

    // --- Private Helper Functions ---

    /**
     * Loads the list of budgets for the currently active period in the state.
     */
    private fun loadBudgets() {
        viewModelScope.launch {
            getBudgetsUseCase(uiState.value.currentPeriod)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { budgetItems ->
                    processBudgetResult(budgetItems)
                }
        }
    }

    /**
     * Processes the result from the budget data source and updates the UI state accordingly.
     */
    private fun processBudgetResult(budgets: List<Budget>) {
        val totalBudgeted = budgets.sumOf { it.budgetAmount }
        val totalSpent = budgets.sumOf { it.spentAmount }
        val categories = budgets.map {
            Category(
                it.categoryId,
                it.categoryName,
                it.categoryIconIdentifier.toString(),
                TransactionType.EXPENSE
            )
        }.distinctBy { it.id }

        _uiState.update {
            it.copy(
                isLoading = false,
                budgets = budgets,
                totalBudgeted = totalBudgeted,
                totalSpent = totalSpent,
                categoriesWithBudget = categories,
            )
        }
    }

    /**
     * Updates the period in the state and then reloads the budget data.
     */
    private fun updatePeriodAndReload(newPeriod: YearMonth) {
        _uiState.update {
            it.copy(
                currentPeriod = newPeriod,
                showMonthPickerDialog = false
            )
        }
        loadBudgets()
    }

    /**
     * Handles navigation to the add/edit budget screen.
     * @param budgetId The ID of the budget to edit, or null if adding a new budget.
     */
    private fun navigateToBudgetScreen(budgetId: String? = null) {
        viewModelScope.launch {
            _navigationEvent.emit(
                BudgetRoutes.AddEditBudget(
                    period = uiState.value.currentPeriod.toString(),
                    budgetId = budgetId
                )
            )
        }
    }

    /**
     * Shows or hides the month picker dialog.
     */
    private fun showMonthPickerDialog(isVisible: Boolean) {
        _uiState.update { it.copy(showMonthPickerDialog = isVisible) }
    }
}