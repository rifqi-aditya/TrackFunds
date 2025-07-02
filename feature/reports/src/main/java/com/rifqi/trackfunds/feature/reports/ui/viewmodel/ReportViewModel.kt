package com.rifqi.trackfunds.feature.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.report.GetCashFlowSummaryUseCase
import com.rifqi.trackfunds.core.domain.usecase.report.GetExpenseBreakdownUseCase
import com.rifqi.trackfunds.core.domain.usecase.report.GetIncomeBreakdownUseCase
import com.rifqi.trackfunds.feature.reports.ui.event.ReportEvent
import com.rifqi.trackfunds.feature.reports.ui.state.ReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getCashFlowSummaryUseCase: GetCashFlowSummaryUseCase,
    private val getExpenseBreakdownUseCase: GetExpenseBreakdownUseCase,
    private val getIncomeBreakdownUseCase: GetIncomeBreakdownUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    val activeBreakdownData: StateFlow<List<CategorySpending>> =
        _uiState.map { state ->
            when (state.activeBreakdownType) {
                TransactionType.EXPENSE -> state.expenseBreakdown
                TransactionType.INCOME -> state.incomeBreakdown
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadReportDataForCurrentPeriod()
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.PeriodChanged -> {
                _uiState.update { it.copy(currentPeriod = event.newPeriod) }
                loadReportDataForCurrentPeriod()
            }

            is ReportEvent.BreakdownTypeSelected -> {
                _uiState.update { it.copy(activeBreakdownType = event.type) }
            }
        }
    }

    private fun loadReportDataForCurrentPeriod() {
        viewModelScope.launch {
            val period = _uiState.value.currentPeriod
            val startDate = period.atDay(1).atStartOfDay()
            val endDate = period.atEndOfMonth().atTime(23, 59, 59)

            // Gabungkan semua Flow dari UseCase menjadi satu
            combine(
                getCashFlowSummaryUseCase(startDate, endDate),
                getExpenseBreakdownUseCase(startDate, endDate),
                getIncomeBreakdownUseCase(startDate, endDate)
            ) { cashFlow, expenseList, incomeList ->
                // Buat objek state baru dengan semua data yang diterima
                _uiState.value.copy(
                    isLoading = false,
                    cashFlowSummary = cashFlow,
                    expenseBreakdown = expenseList,
                    incomeBreakdown = incomeList
                )
            }
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { newState ->
                    // Update state utama dengan hasil gabungan
                    _uiState.value = newState
                }
        }
    }
}