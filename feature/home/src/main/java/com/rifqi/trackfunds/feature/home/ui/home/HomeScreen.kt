package com.rifqi.trackfunds.feature.home.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetRoutes
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.home.ui.home.components.BudgetCard
import com.rifqi.trackfunds.feature.home.ui.home.components.HomeHeader
import com.rifqi.trackfunds.feature.home.ui.home.components.RecentTransactionsCard
import com.rifqi.trackfunds.feature.home.ui.home.components.SummaryCard
import com.rifqi.trackfunds.feature.home.ui.home.components.TotalBalanceCard
import com.rifqi.trackfunds.feature.home.ui.preview.HomeUiStatePreviewParameterProvider
import kotlinx.coroutines.flow.collectLatest


/**
 * Stateful Composable (Container)
 * - Bertanggung jawab untuk mendapatkan ViewModel dan state.
 * - Menghubungkan logika ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                HomeSideEffect.NavigateBack -> {
                    onNavigateBack()
                }

                HomeSideEffect.NavigateToAccounts -> {
                    onNavigate(AccountRoutes.Accounts)
                }

                HomeSideEffect.NavigateToBudgets -> {
                    onNavigate(BudgetRoutes.Budgets)
                }

                HomeSideEffect.NavigateToTransactions -> {
                    onNavigate(TransactionRoutes.Transactions)
                }

                HomeSideEffect.NavigateToNotifications -> {

                }

                is HomeSideEffect.NavigateToTransactionDetails -> {
                    onNavigate(
                        TransactionRoutes.TransactionDetail(
                            transactionId = sideEffect.transactionId
                        )
                    )
                }

                is HomeSideEffect.ShowSnackbar -> {

                }

                HomeSideEffect.NavigateToProfile -> {
                    onNavigate(
                        ProfileRoutes.Settings
                    )
                }
            }
        }
    }

    HomeScreenContent(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

/**
 * Stateless Composable (Presentational)
 * - Hanya menerima state dan lambda.
 * - Tidak tahu tentang ViewModel atau Hilt.
 * - Sangat mudah untuk di-preview dan diuji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    HomeHeader(
                        userName = "Rifqi Aditya",
                        onProfileClick = { onEvent(HomeEvent.ProfileClicked) },
                        onNotificationsClick = {},
                    )
                }
            }

            item {
                TotalBalanceCard(
                    totalBalance = state.totalBalance,
                    onAccountClicked = {
                        onEvent(HomeEvent.AccountsClicked)
                    },
                    onInfoClicked = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = "Total Income",
                        amount = formatCurrency(state.totalIncome),
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        iconBackgroundColor = TrackFundsTheme.extendedColors.income,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Total Expense",
                        amount = formatCurrency(state.totalExpense),
                        icon = Icons.AutoMirrored.Filled.TrendingDown,
                        iconBackgroundColor = TrackFundsTheme.extendedColors.expense,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                BudgetCard(
                    spentAmount = state.totalBudgetSpent,
                    remainingAmount = state.totalBudgetRemaining,
                    onSeeAllClick = { onEvent(HomeEvent.AllBudgetsClicked) },
                    progress = state.budgetProgress,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    RecentTransactionsCard(
                        uiState = state,
                        onEvent = onEvent,
                        onViewAllClick = { onEvent(HomeEvent.AllTransactionsClicked) },
                        onTransactionClick = { transactionId ->
                            onEvent(HomeEvent.TransactionClicked(transactionId))
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Home Screen States")
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(HomeUiStatePreviewParameterProvider::class) uiState: HomeUiState
) {
    TrackFundsTheme {
        HomeScreenContent(
            state = uiState,
            onEvent = {}
        )
    }
}