package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.ScanReceipt
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.mapper.toHomeCategorySummary
import com.rifqi.trackfunds.feature.home.ui.state.HomeSummary
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategorySummariesUseCase: GetCategorySummariesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // FIX 1: Tambahkan SharedFlow untuk event navigasi
    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadData()
    }

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                // Menangani navigasi
                HomeEvent.FabClicked -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = true) }
                }

                HomeEvent.AddActionDialogDismissed -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = false) }
                }

                HomeEvent.AddTransactionManuallyClicked -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = false) }
                    _navigationEvent.emit(AddEditTransaction())
                }

                HomeEvent.ScanReceiptClicked -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = false) }
                    _navigationEvent.emit(ScanReceipt)
                }

                HomeEvent.AllTransactionsClicked -> _navigationEvent.emit(AllTransactions)
                HomeEvent.NotificationsClicked -> _navigationEvent.emit(Notifications)
                is HomeEvent.CategorySummaryClicked -> _navigationEvent.emit(
                    CategoryTransactions(
                        categoryId = event.item.categoryId,
                        categoryName = event.item.categoryName
                    )
                )

                is HomeEvent.TypedTransactionsClicked -> _navigationEvent.emit(
                    TypedTransactions(
                        event.type
                    )
                )

                // Menangani perubahan state
                HomeEvent.ChangePeriodClicked -> _uiState.update { it.copy(showMonthPickerDialog = true) }
                HomeEvent.DialogDismissed -> _uiState.update { it.copy(showMonthPickerDialog = false) }
                is HomeEvent.PeriodChanged -> {
                    val newStartDate = event.newPeriod.atDay(1)
                    val newEndDate = event.newPeriod.atEndOfMonth()
                    _uiState.update {
                        it.copy(
                            dateRangePeriod = Pair(newStartDate, newEndDate),
                            currentMonthAndYear = event.newPeriod.format(
                                DateTimeFormatter.ofPattern(
                                    "MMMM yyyy"
                                )
                            ),
                            showMonthPickerDialog = false
                        )
                    }
                    loadData()
                }
            }
        }
    }

    // loadData() tidak berubah secara signifikan
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
}