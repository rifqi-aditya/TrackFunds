package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.state.HomeCategorySummaryItem
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * Manages the UI state and business logic for the HomeScreen.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val getTopBudgetsUseCase: GetTopBudgetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        // Amati perubahan periode dan muat ulang data secara otomatis
        viewModelScope.launch {
            _uiState.map { it.dateRangePeriod }
                .distinctUntilChanged()
                .flatMapLatest { dateRange ->
                    loadDataForPeriod(dateRange.first, dateRange.second)
                }
                .collect { resultState ->
                    _uiState.update { resultState }
                }
        }
    }

    /**
     * Handles all user actions from the UI.
     * @param event The event triggered by the user.
     */
    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                // Navigation Events
                HomeEvent.AddTransactionManuallyClicked -> {
                    _navigationEvent.emit(AddEditTransaction())
                }

                HomeEvent.ScanReceiptClicked -> {
                    _navigationEvent.emit(ScanGraph)
                }

                HomeEvent.AddActionDialogDismissed -> _uiState.update {
                    it.copy(
                        isAddActionDialogVisible = false
                    )
                }

                HomeEvent.AllTransactionsClicked -> _navigationEvent.emit(AllTransactions)
                HomeEvent.BudgetsScreenClicked -> _navigationEvent.emit(BudgetsGraph)
                HomeEvent.NotificationsClicked -> _navigationEvent.emit(Notifications)

                is HomeEvent.CategorySummaryClicked -> _navigationEvent.emit(
                    CategoryTransactions(event.item.categoryId, event.item.categoryName)
                )

                is HomeEvent.TypedTransactionsClicked -> _navigationEvent.emit(
                    TypedTransactions(event.type)
                )

                is HomeEvent.TransactionClicked -> _navigationEvent.emit(
                    TransactionDetail(event.transactionId)
                )

                // State Management Events
                HomeEvent.FabClicked -> _uiState.update { it.copy(isAddActionDialogVisible = true) }
                HomeEvent.ChangePeriodClicked -> _uiState.update { it.copy(showMonthPickerDialog = true) }
                HomeEvent.DialogDismissed -> _uiState.update { it.copy(showMonthPickerDialog = false) }
                is HomeEvent.PeriodChanged -> changePeriod(event.newPeriod)

            }
        }
    }

    private fun changePeriod(newPeriod: YearMonth) {
        val newStartDate = newPeriod.atDay(1)
        val newEndDate = newPeriod.atEndOfMonth()
        _uiState.update {
            it.copy(
                dateRangePeriod = Pair(newStartDate, newEndDate),
                currentMonthAndYear = newPeriod.format(
                    DateTimeFormatter.ofPattern(
                        "MMMM yyyy",
                        Locale.getDefault()
                    )
                ),
                showMonthPickerDialog = false
            )
        }
    }

    private fun loadDataForPeriod(startDate: LocalDate, endDate: LocalDate): Flow<HomeUiState> {
        // Buat filter dasar untuk periode ini
        val baseFilter = TransactionFilter(startDate = startDate, endDate = endDate)

        // Panggil UseCase dengan filter yang berbeda untuk setiap kebutuhan
        val recentFlow = getFilteredTransactionsUseCase(baseFilter.copy(limit = 5)) // Ambil 5 transaksi terakhir
        val incomeFlow = getFilteredTransactionsUseCase(baseFilter.copy(type = TransactionType.INCOME))
        val expenseFlow = getFilteredTransactionsUseCase(baseFilter.copy(type = TransactionType.EXPENSE))
        val topBudgetsFlow = getTopBudgetsUseCase(YearMonth.from(startDate))

        return combine(
            incomeFlow,
            expenseFlow,
            recentFlow,
            topBudgetsFlow,
        ) { incomeList, expenseList, recentList, topBudgetsList ->
            // Logika kalkulasi tetap sama
            val totalIncome = incomeList.sumOf { it.amount }
            val totalExpenses = expenseList.sumOf { it.amount }

            _uiState.value.copy(
                isLoading = false,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                // Kita sekarang perlu membuat ringkasan kategori secara manual di sini
                incomeSummaries = incomeList.toCategorySummary(),
                expenseSummaries = expenseList.toCategorySummary(),
                recentTransactions = recentList,
                topBudgets = topBudgetsList,
                error = null
            )
        }
            .onStart { emit(_uiState.value.copy(isLoading = true)) }
            .catch { e -> emit(_uiState.value.copy(isLoading = false, error = e.message)) }
    }

    // Fungsi helper baru untuk membuat ringkasan
    private fun List<TransactionItem>.toCategorySummary(): List<HomeCategorySummaryItem> {
        return this.groupBy { it.category }
            .map { (category, transactions) ->
                HomeCategorySummaryItem(
                    categoryId = category?.id ?: "",
                    categoryName = category?.name ?: "Uncategorized",
                    categoryIconIdentifier = category?.iconIdentifier,
                    transactionType = transactions.first().type,
                    totalAmount = transactions.sumOf { it.amount }
                )
            }
            .sortedByDescending { it.totalAmount }
    }
}