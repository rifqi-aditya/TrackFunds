package com.rifqi.trackfunds.feature.budget.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditBudget
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetListEvent
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetListUiState
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
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetListUiState())
    val uiState: StateFlow<BudgetListUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadBudgets()
    }

    fun onEvent(event: BudgetListEvent) {
        when (event) {
            is BudgetListEvent.ChangePeriod -> {
                _uiState.update { it.copy(currentPeriod = event.newPeriod) }
                loadBudgets()
            }
            // FIX 2: Tangani event klik tombol
            BudgetListEvent.AddBudgetClicked -> {
                viewModelScope.launch {
                    val periodString =
                        _uiState.value.currentPeriod.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    // Kirim sinyal untuk navigasi ke mode Add (budgetId = null)
                    _navigationEvent.emit(AddEditBudget(period = periodString))
                }
            }

            is BudgetListEvent.EditBudgetClicked -> {
                viewModelScope.launch {
                    val periodString =
                        _uiState.value.currentPeriod.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    // Kirim sinyal untuk navigasi ke mode Edit dengan budgetId
                    _navigationEvent.emit(
                        AddEditBudget(
                            period = periodString,
                            budgetId = event.budgetId
                        )
                    )
                }
            }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            val period = _uiState.value.currentPeriod

            getBudgetsUseCase(period)
                .onStart {
                    // Tampilkan loading dan hapus error sebelumnya
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    // Jika ada error, hentikan loading dan tampilkan pesan
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { budgetItems ->
                    // Hitung total dari daftar budget yang diterima
                    val totalBudgeted = budgetItems.mapNotNull { it.budgetAmount }.sumOf { it }
                    val totalSpent = budgetItems.sumOf { it.spentAmount }

                    // Update state dengan data yang berhasil dimuat
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            budgets = budgetItems,
                            totalBudgeted = totalBudgeted,
                            totalSpent = totalSpent
                        )
                    }
                }
        }
    }
}