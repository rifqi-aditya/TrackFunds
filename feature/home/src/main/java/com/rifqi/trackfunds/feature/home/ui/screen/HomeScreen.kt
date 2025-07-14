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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.ui.components.AddTransactionDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.home.ui.components.BudgetSummaryRow
import com.rifqi.trackfunds.feature.home.ui.components.HomeHeader
import com.rifqi.trackfunds.feature.home.ui.components.InsightCard
import com.rifqi.trackfunds.feature.home.ui.components.RecentTransactionRow
import com.rifqi.trackfunds.feature.home.ui.components.SummarySection
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal
import java.time.LocalDateTime


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

    if (uiState.isAddActionDialogVisible) {
        AddTransactionDialog(
            onDismissRequest = { viewModel.onEvent(HomeEvent.AddActionDialogDismissed) },
            onScanClick = { viewModel.onEvent(HomeEvent.ScanReceiptClicked) },
            onAddManuallyClick = { viewModel.onEvent(HomeEvent.AddTransactionManuallyClicked) }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is Home) {
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
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

                    HomeHeader(
                        userName = "Rifqi Aditya",
                        onProfileClick = { onEvent(HomeEvent.ProfileClicked)},
                        onNotificationsClick = {},
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InsightCard(
                        totalExpenseThisMonth = uiState.totalExpenseThisMonth,
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
                Column(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SummarySection(
                        title = "Recent transactions",
                        items = uiState.recentTransactions,
                        onViewAllClick = {
                            onEvent(HomeEvent.AllTransactionsClicked)
                        },
                        onItemClick = { transactionItem ->
                            onEvent(HomeEvent.TransactionClicked(transactionItem.id))
                        },
                        itemContent = { transactionItem ->
                            RecentTransactionRow(
                                item = transactionItem,
                            )
                        }
                    )
                    SummarySection(
                        title = "Your budgets",
                        items = uiState.topBudgets,
                        onViewAllClick = {
                            onEvent(HomeEvent.AllBudgetsClicked)
                        },
                        itemContent = { topBudgetItem ->
                            BudgetSummaryRow(
                                item = topBudgetItem,
                            )
                        }
                    )

                }
            }
        }

        FloatingActionButton(
            onClick = { onEvent(HomeEvent.FabClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            DisplayIconFromResource(
                identifier = "plus",
                contentDescription = "Add Transaction",
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}

private val previewDummyAccount1 =
    AccountItem("acc1", "Dompet Digital", "wallet_account", BigDecimal("1500000"))
private val previewDummyAccount2 =
    AccountItem("acc2", "Rekening Utama", "bank_account", BigDecimal("10000000"))

private val previewDummyCategory1 =
    CategoryItem("cat1", "Makan & Minum", "restaurant", TransactionType.EXPENSE)
private val previewDummyCategory2 = CategoryItem("cat2", "Gaji", "salary", TransactionType.INCOME)

private val previewTransactions = listOf(
    // Transaksi Pengeluaran
    TransactionItem(
        id = "1",
        amount = BigDecimal("50000"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now(),
        description = "Kopi Pagi",
        category = previewDummyCategory1,
        account = previewDummyAccount1
    ),
    // Transaksi Pemasukan
    TransactionItem(
        id = "2",
        amount = BigDecimal("7500000"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1),
        description = "Gaji Bulanan",
        category = previewDummyCategory2,
        account = previewDummyAccount2
    )
)

private val previewBudgets = listOf(
    BudgetItem(
        budgetId = "b1",
        categoryName = "Makan & Minum",
        budgetAmount = BigDecimal("2000000"),
        spentAmount = BigDecimal("1250000"),
        categoryId = previewDummyCategory1.name,
        categoryIconIdentifier = previewDummyCategory1.iconIdentifier,
        period = ""
    ),
    BudgetItem(
        budgetId = "b2",
        categoryName = "Transportasi",
        budgetAmount = BigDecimal("700000"),
        spentAmount = BigDecimal("1550000"),
        categoryId = previewDummyCategory2.name,
        categoryIconIdentifier = previewDummyCategory2.iconIdentifier,
        period = ""
    )
)

private val previewLoadedState = HomeUiState(
    isLoading = false,
    recentTransactions = previewTransactions,
    topBudgets = previewBudgets
)

// --- FUNGSI PREVIEW ---
@Preview(showBackground = true, name = "Home Screen - Loaded")
@Composable
private fun HomeScreenContentLoadedPreview() {
    TrackFundsTheme {
        HomeScreenContent(
            uiState = previewLoadedState,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Loading")
@Composable
private fun HomeScreenContentLoadingPreview() {
    TrackFundsTheme {
        HomeScreenContent(
            uiState = HomeUiState(isLoading = true),
            onEvent = {}
        )
    }
}
