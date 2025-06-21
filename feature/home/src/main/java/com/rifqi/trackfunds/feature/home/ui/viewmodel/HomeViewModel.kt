package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCase
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRangePair
import com.rifqi.trackfunds.core.ui.util.getCurrentMonthAndYear
import com.rifqi.trackfunds.feature.home.ui.mapper.toHomeCategorySummary
import com.rifqi.trackfunds.feature.home.ui.model.HomeSummary
import com.rifqi.trackfunds.feature.home.ui.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategorySummariesUseCase: GetCategorySummariesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = true,
            currentMonthAndYear = getCurrentMonthAndYear(),
            dateRangePeriod = getCurrentDateRangePair()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            //Hitung startDate dan endDate dari uiState
            val dateRange = _uiState.value.dateRangePeriod
            val startDate = dateRange.first.atStartOfDay()
            val endDate = dateRange.second.atTime(23, 59, 59)

            // Panggil UseCase yang BENAR dengan parameter tanggal
            val incomeSummariesFlow =
                getCategorySummariesUseCase(TransactionType.INCOME, startDate, endDate)
            val expenseSummariesFlow =
                getCategorySummariesUseCase(TransactionType.EXPENSE, startDate, endDate)

            //Gabungkan kedua flow untuk mendapatkan data ringkasan
            combine(incomeSummariesFlow, expenseSummariesFlow) { incomeList, expenseList ->
                val totalIncome = incomeList.sumOf { it.totalAmount }
                val totalExpenses = expenseList.sumOf { it.totalAmount }
                val monthlyBalance = totalIncome.subtract(totalExpenses)

                HomeSummary(
                    monthlyBalance = monthlyBalance,
                    totalExpenses = totalExpenses,
                    totalIncome = totalIncome,
                    expenseSummaries = expenseList.map { it.toHomeCategorySummary() },
                    incomeSummaries = incomeList.map { it.toHomeCategorySummary() }
                )
            }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "An unknown error occurred"
                        )
                    }
                }
                .collect { summary ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            summary = summary
                        )
                    }
                }
        }
    }

    fun onMonthSelected(selectedYearMonth: YearMonth) {
        val newStartDate = selectedYearMonth.atDay(1)
        val newEndDate = selectedYearMonth.atEndOfMonth()

        _uiState.update {
            it.copy(
                dateRangePeriod = Pair(newStartDate, newEndDate),
                currentMonthAndYear = selectedYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            )
        }

        loadData()
    }

    fun onDateRangeSelectorClicked() {
        _uiState.update { it.copy(showMonthPickerDialog = true) }
    }

    fun onMonthDialogDismissed() {
        _uiState.update { it.copy(showMonthPickerDialog = false) }
    }

    fun onNotificationsClicked() { /* ... */
    }

    fun onSummaryActionClicked() { /* ... */
    }

    fun onMoreOptionsClicked() { /* ... */
    }

    fun onBalanceCardClicked() { /* ... */
    }

    fun onFabClicked() { /* ... */
    }
}