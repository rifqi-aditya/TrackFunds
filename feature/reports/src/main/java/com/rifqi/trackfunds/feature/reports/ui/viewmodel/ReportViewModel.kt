package com.rifqi.trackfunds.feature.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.model.CashFlowSummary
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.feature.reports.ui.event.ReportEvent
import com.rifqi.trackfunds.feature.reports.ui.state.ReportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    val activeBreakdownData: StateFlow<List<CategorySpending>> =
        _uiState.map { state ->
            when (state.activeBreakdownType) {
                TransactionType.EXPENSE -> state.expenseBreakdown
                TransactionType.INCOME -> state.incomeBreakdown
                else -> emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            _uiState
                .map { it.dateRange }
                .distinctUntilChanged()
                .onEach { dateRange ->
                    loadDataForPeriod(dateRange.first, dateRange.second)
                }
                .collect()
        }
    }

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.PeriodChanged -> {
                _uiState.update { it.copy(currentPeriod = event.newPeriod) }
            }

            is ReportEvent.BreakdownTypeSelected -> {
                _uiState.update { it.copy(activeBreakdownType = event.type) }
            }

            ReportEvent.ChangePeriodClicked -> _uiState.update { it.copy(showPeriodSheet = true) }
            ReportEvent.PeriodSheetDismissed -> _uiState.update { it.copy(showPeriodSheet = false) }

            is ReportEvent.DateOptionSelected -> {
                val (startDate, endDate) = calculateDateRangeFromOption(event.option)
                _uiState.update {
                    it.copy(
                        dateRange = Pair(startDate, endDate),
                        selectedDateOption = event.option,
                        showPeriodSheet = false
                    )
                }
            }

            ReportEvent.CustomDateRangeClicked -> {
                _uiState.update { it.copy(showDatePicker = true, showPeriodSheet = false) }
            }

            ReportEvent.DatePickerDismissed -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is ReportEvent.DateRangeSelected -> {
                _uiState.update {
                    it.copy(
                        dateRange = Pair(event.startDate, event.endDate),
                        customStartDate = event.startDate,
                        customEndDate = event.endDate,
                        selectedDateOption = DateRangeOption.CUSTOM,
                        showDatePicker = false
                    )
                }
            }
        }
    }

    private fun loadDataForPeriod(startDate: LocalDate?, endDate: LocalDate?) {
        viewModelScope.launch {
            val filter = TransactionFilter(startDate = startDate, endDate = endDate)

            getFilteredTransactionsUseCase(filter)
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    val totalIncome = transactions.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    val totalSavings = transactions.filter { it.type == TransactionType.SAVINGS }
                        .sumOf { it.amount }

                    val expenseBreakdown = transactions
                        .filter { it.type == TransactionType.EXPENSE && it.category != null }
                        .groupBy { it.category!! }
                        .map { (category, transactionList) ->
                            CategorySpending(category.name, transactionList.sumOf { it.amount })
                        }
                        .sortedByDescending { it.totalAmount }

                    val incomeBreakdown = transactions
                        .filter { it.type == TransactionType.INCOME && it.category != null }
                        .groupBy { it.category!! }
                        .map { (category, transactionList) ->
                            CategorySpending(category.name, transactionList.sumOf { it.amount })
                        }
                        .sortedByDescending { it.totalAmount }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            cashFlowSummary = CashFlowSummary(
                                totalIncome,
                                totalExpense,
                                totalSavings,
                                netCashFlow = totalIncome.subtract(totalExpense)
                            ),
                            expenseBreakdown = expenseBreakdown,
                            incomeBreakdown = incomeBreakdown
                        )
                    }
                }
        }
    }

    private fun calculateDateRangeFromOption(option: DateRangeOption): Pair<LocalDate?, LocalDate?> {
        val today = LocalDate.now()
        return when (option) {
            DateRangeOption.ALL_TIME -> Pair(null, null)
            DateRangeOption.TODAY -> Pair(today, today)
            DateRangeOption.LAST_7_DAYS -> Pair(today.minusDays(6), today)
            DateRangeOption.LAST_30_DAYS -> Pair(today.minusMonths(1).plusDays(1), today)
            DateRangeOption.LAST_90_DAYS -> Pair(today.minusMonths(3).plusDays(1), today)
            DateRangeOption.CUSTOM -> Pair(null, null) // TODO: Perbaiki
        }
    }
}