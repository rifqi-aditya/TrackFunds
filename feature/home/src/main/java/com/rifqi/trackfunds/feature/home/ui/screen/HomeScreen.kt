package com.rifqi.trackfunds.feature.home.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.HomeRoutes
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.home.ui.components.HomeHeader
import com.rifqi.trackfunds.feature.home.ui.components.InsightCard
import com.rifqi.trackfunds.feature.home.ui.components.RecentTransactionsCard
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.preview.HomeUiStatePreviewParameterProvider
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.HomeViewModel
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

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is HomeRoutes.Home) {
                onNavigateBack()
            } else {
                onNavigate(screen)
            }
        }
    }

    HomeScreenContent(
        uiState = uiState,
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
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))
                    Column() {
                        Text(
                            "Balance",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                        )
                        Text(
                            formatCurrency(uiState.totalBalance),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TrackFundsTheme.extendedColors.accentGreen
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    InsightCard(
                        totalBalance = uiState.totalBalance,
                        totalSavings = uiState.totalSavings,
                        totalAccounts = uiState.totalAccounts,
                        onBalanceClick = { onEvent(HomeEvent.BalanceClicked) },
                        onSavingsClick = { onEvent(HomeEvent.SavingsClicked) },
                        onAccountsClick = { onEvent(HomeEvent.AccountsClicked) },
                    )
                }
            }

            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    RecentTransactionsCard(
                        uiState = uiState,
                        onEvent = onEvent,
                        onViewAllClick = { onEvent(HomeEvent.AllTransactionsClicked) },
                        onTransactionClick = { transactionId ->
                            onEvent(HomeEvent.TransactionClicked(transactionId))
                        }
                    )
//                    SummarySection(
//                        title = "Remaining budgets",
//                        items = uiState.topBudgets,
//                        onViewAllClick = {
//                            onEvent(HomeEvent.AllBudgetsClicked)
//                        },
//                        itemContent = { topBudgetItem ->
//                            BudgetSummaryRow(
//                                item = topBudgetItem,
//                            )
//                        }
//                    )
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
            uiState = uiState,
            onEvent = {}
        )
    }
}