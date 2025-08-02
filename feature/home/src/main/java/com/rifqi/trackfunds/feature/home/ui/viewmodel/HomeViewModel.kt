package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.savings.model.SavingsFilter
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetFilteredSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes.TransactionDetail
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
                HomeEvent.AllTransactionsClicked -> _navigationEvent.emit(TransactionsGraph)
                HomeEvent.AllBudgetsClicked -> _navigationEvent.emit(BudgetsGraph)
                HomeEvent.NotificationsClicked -> _navigationEvent.emit(ProfileRoutes.Notifications)
                HomeEvent.AccountsClicked -> _navigationEvent.emit(AccountsGraph)
                HomeEvent.BalanceClicked -> _navigationEvent.emit(AccountsGraph)
                HomeEvent.ProfileClicked -> _navigationEvent.emit(ProfileGraph)
                HomeEvent.AllSavingsGoalsClicked -> _navigationEvent.emit(SavingsGraph)

                is HomeEvent.TransactionClicked -> _navigationEvent.emit(
                    TransactionDetail(event.transactionId)
                )

                is HomeEvent.TabSelected -> {
                    _uiState.update { it.copy(selectedTabIndex = event.index) }
                }
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
                getFilteredTransactionsUseCase(TransactionFilter()),
                getTopBudgetsUseCase(YearMonth.from(startDate))
            ) { allAccounts, allSavings, recentTransaction, topBudgets ->

                val totalBalance = allAccounts.sumOf { it.balance }
                val totalSavings = allSavings.sumOf { it.savedAmount }
                val recentExpenses = recentTransaction
                    .filter { it.type == TransactionType.EXPENSE }
                    .take(3)
                val recentIncomes = recentTransaction
                    .filter { it.type == TransactionType.INCOME }
                    .take(3)
                val recentSavings = recentTransaction
                    .filter { it.type == TransactionType.SAVINGS }
                    .take(3)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalBalance = totalBalance,
                        totalSavings = totalSavings,
                        totalAccounts = allAccounts.size,
                        recentSavingsTransactions = recentSavings,
                        recentExpenseTransactions = recentExpenses,
                        recentIncomeTransactions = recentIncomes,
                        topBudgets = topBudgets
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect()
        }
    }
}