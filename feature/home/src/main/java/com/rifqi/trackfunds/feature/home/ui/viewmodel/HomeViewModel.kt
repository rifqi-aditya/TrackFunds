package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetFilteredSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.TransactionsGraph
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/**
 * Manages the UI state and business logic for the HomeScreen.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val getFilteredSavingsGoalsUseCase: GetFilteredSavingsGoalsUseCase,
    private val getTopBudgetsUseCase: GetTopBudgetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadHomeScreenData()
    }

    /**
     * Handles all user actions from the UI.
     * @param event The event triggered by the user.
     */
    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                // Navigation Events
                HomeEvent.AddActionDialogDismissed -> _uiState.update {
                    it.copy(
                        isAddActionDialogVisible = false
                    )
                }

                HomeEvent.AddTransactionManuallyClicked -> _navigationEvent.emit(AddEditTransaction())
                HomeEvent.ScanReceiptClicked -> _navigationEvent.emit(ScanGraph)
                HomeEvent.AllTransactionsClicked -> _navigationEvent.emit(TransactionsGraph)
                HomeEvent.AllBudgetsClicked -> _navigationEvent.emit(BudgetsGraph)
                HomeEvent.NotificationsClicked -> _navigationEvent.emit(Notifications)
                HomeEvent.AccountsClicked -> _navigationEvent.emit(AccountsGraph)
                HomeEvent.BalanceClicked -> _navigationEvent.emit(AccountsGraph)
                HomeEvent.SavingsClicked -> _navigationEvent.emit(SavingsGraph)
                HomeEvent.ProfileClicked -> _navigationEvent.emit(ProfileGraph)

                is HomeEvent.TransactionClicked -> _navigationEvent.emit(
                    TransactionDetail(event.transactionId)
                )

                // State Management Events
                HomeEvent.FabClicked -> _uiState.update { it.copy(isAddActionDialogVisible = true) }
            }
        }
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Tentukan periode "Bulan Ini"
            val today = LocalDate.now()
            val startDate = today.withDayOfMonth(1)
            val endDate = today.with(TemporalAdjusters.lastDayOfMonth())

            // Buat filter untuk mengambil data bulan ini
            val monthlyFilter = TransactionFilter(startDate = startDate, endDate = endDate)

            // Gabungkan semua data yang dibutuhkan
            combine(
                getAccountsUseCase(),
                getFilteredSavingsGoalsUseCase(SavingsFilter()),
                getFilteredTransactionsUseCase(monthlyFilter),
                getTopBudgetsUseCase(YearMonth.from(startDate))
            ) { allAccounts, allSavings, monthlyTransactions, topBudgets ->

                val totalBalance = allAccounts.sumOf { it.balance }
                val totalSavings = allSavings.sumOf { it.currentAmount }
                val totalExpenseThisMonth = monthlyTransactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalBalance = totalBalance,
                        totalSavings = totalSavings,
                        totalAccounts = allAccounts.size,
                        totalExpenseThisMonth = totalExpenseThisMonth,
                        recentTransactions = monthlyTransactions.take(3),
                        topBudgets = topBudgets
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect()
        }
    }
}