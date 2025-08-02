package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.GetBudgetsUseCase
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.sideeffect.HomeSideEffect
import com.rifqi.trackfunds.feature.home.ui.sideeffect.HomeSideEffect.NavigateToTransactionDetails
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
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
    private val getBudgetsUseCase: GetBudgetsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

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
                HomeEvent.AllTransactionsClicked -> _sideEffect.send(HomeSideEffect.NavigateToTransactions)
                HomeEvent.AllBudgetsClicked -> _sideEffect.send(HomeSideEffect.NavigateToBudgets)
                HomeEvent.NotificationsClicked -> _sideEffect.send(HomeSideEffect.NavigateToNotifications)
                HomeEvent.AccountsClicked -> _sideEffect.send(HomeSideEffect.NavigateToAccounts)
                HomeEvent.ProfileClicked -> _sideEffect.send(HomeSideEffect.NavigateToProfile)

                is HomeEvent.TransactionClicked -> _sideEffect.send(
                    NavigateToTransactionDetails(
                        transactionId = event.transactionId
                    )
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

            // Tentukan periode "Bulan Ini" dengan benar
            val today = LocalDate.now()
            val startDate = today.withDayOfMonth(1)
            val endDate = today.with(TemporalAdjusters.lastDayOfMonth())
            val monthlyFilter = TransactionFilter(startDate = startDate, endDate = endDate)

            // Kita hanya perlu mengambil transaksi untuk bulan ini
            val monthlyTransactionsFlow = getFilteredTransactionsUseCase(monthlyFilter)
            val monthlyBudgetsFlow = getBudgetsUseCase(period = YearMonth.now())
            val allAccountsFlow = getAccountsUseCase()

            combine(
                monthlyTransactionsFlow,
                allAccountsFlow,
                monthlyBudgetsFlow
            ) { monthlyTransactions, allAccounts, monthlyBudgets ->

                val totalBalance = allAccounts.sumOf { it.balance }

                val totalIncome = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }

                val totalExpense = monthlyTransactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                val totalBudgetAmount = monthlyBudgets.sumOf { it.budgetAmount }

                val totalBudgetSpent = monthlyBudgets.sumOf { it.spentAmount }

                val totalBudgetRemaining = totalBudgetAmount - totalBudgetSpent

                val budgetProgress = if (totalBudgetAmount > BigDecimal.ZERO) {
                    (totalBudgetSpent.toFloat() / totalBudgetAmount.toFloat()).coerceIn(0f, 1f)
                } else {
                    0f
                }

                val recentExpenses = monthlyTransactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .take(5)

                val recentIncome = monthlyTransactions
                    .filter { it.type == TransactionType.INCOME }
                    .take(5)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        totalBalance = totalBalance,
                        totalIncome = totalIncome,
                        totalExpense = totalExpense,
                        recentExpenseTransactions = recentExpenses,
                        recentIncomeTransactions = recentIncome,
                        totalBudgetSpent = totalBudgetSpent,
                        totalBudgetRemaining = totalBudgetRemaining,
                        budgetProgress = budgetProgress,
                        error = null
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect()
        }
    }
}