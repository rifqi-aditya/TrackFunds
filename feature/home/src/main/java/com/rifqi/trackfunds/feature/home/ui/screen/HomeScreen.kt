package com.rifqi.trackfunds.feature.home.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.core.ui.components.AddTransactionDialog
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatDateRangeToString
import com.rifqi.trackfunds.feature.home.ui.components.BalanceCard
import com.rifqi.trackfunds.feature.home.ui.components.BudgetSummaryRow
import com.rifqi.trackfunds.feature.home.ui.components.CategorySummaryRow
import com.rifqi.trackfunds.feature.home.ui.components.RecentTransactionRow
import com.rifqi.trackfunds.feature.home.ui.components.SummarySection
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.HomeViewModel
import java.time.YearMonth


/**
 * Stateful Composable (Container)
 * - Bertanggung jawab untuk mendapatkan ViewModel dan state.
 * - Menghubungkan logika ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAllTransactions: () -> Unit,
    onNavigateToCategoryTransactions: (categoryId: String, categoryName: String) -> Unit,
    onNavigateToTypedTransactions: (TransactionType) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToScanReceipt: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isAddActionDialogVisible) { // State ini sekarang mengontrol dialog
        AddTransactionDialog(
            onDismissRequest = { viewModel.onEvent(HomeEvent.AddActionDialogDismissed) },
            onScanClick = { viewModel.onEvent(HomeEvent.ScanReceiptClicked) },
            onAddManuallyClick = { viewModel.onEvent(HomeEvent.AddTransactionManuallyClicked) }
        )
    }
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            when (screen) {
                is AllTransactions -> onNavigateToAllTransactions()
                is CategoryTransactions -> onNavigateToCategoryTransactions(
                    screen.categoryId,
                    screen.categoryName
                )

                is TypedTransactions -> onNavigateToTypedTransactions(screen.transactionType)
                is Notifications -> onNavigateToNotifications()
                is AddEditTransaction -> onNavigateToAddTransaction()
                is ScanGraph -> onNavigateToScanReceipt()
                else -> {}
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
    val dateRangeString = formatDateRangeToString(uiState.dateRangePeriod)

    MonthYearPickerDialog(
        showDialog = uiState.showMonthPickerDialog,
        initialYearMonth = YearMonth.from(uiState.dateRangePeriod.first),
        onDismiss = { onEvent(HomeEvent.DialogDismissed) },
        onConfirm = { onEvent(HomeEvent.PeriodChanged(it)) }
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    navigationIcon = {
                        DisplayIconFromResource(
                            identifier = "calendar",
                            contentDescription = "Change Period",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onEvent(HomeEvent.ChangePeriodClicked) }
                                )
                        )
                    },
                    title = {
                        Column(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onEvent(HomeEvent.ChangePeriodClicked) }
                            )
                        ) {
                            Text(
                                uiState.currentMonthAndYear,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                dateRangeString,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { onEvent(HomeEvent.NotificationsClicked) }) {
                            DisplayIconFromResource(
                                identifier = "notifications",
                                contentDescription = "Notifications",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Error: ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(top = innerPadding.calculateTopPadding())
                ) {
                    item {
                        BalanceCard(
                            onClick = { onEvent(HomeEvent.AllTransactionsClicked) },
                            monthlyBalance = uiState.monthlyBalance,
                            totalExpenses = uiState.totalExpenses,
                            totalIncome = uiState.totalIncome
                        )
                    }
                    item {
                        SummarySection(
                            title = "Recent Transactions",
                            items = uiState.recentTransactions, // Gunakan data baru dari state
                            onViewAllClick = {
                                onEvent(HomeEvent.AllTransactionsClicked)
                            },
                            onItemClick = { transactionItem ->
                                // Kirim event untuk navigasi ke detail transaksi
                                onEvent(HomeEvent.RecentTransactionItemClicked(transactionItem.id))
                            },
                            itemContent = { transactionItem ->
                                // Gunakan TransactionRow yang sudah Anda buat sebelumnya
                                RecentTransactionRow(
                                    transaction = transactionItem,
                                    onClick = { },
                                )
                            }
                        )
                    }
                    item {
                        SummarySection(
                            title = "Your Budgets",
                            items = uiState.topBudgets, // Gunakan data baru dari state
                            onViewAllClick = {
                                onEvent(HomeEvent.AllTransactionsClicked)
                            },
                            onItemClick = { transactionItem ->
                                // Kirim event untuk navigasi ke detail transaksi
//                                onEvent(HomeEvent.TransactionClicked(transactionItem.id))
                            },
                            itemContent = { topBudgetItem ->
                                // Gunakan TransactionRow yang sudah Anda buat sebelumnya
                                BudgetSummaryRow(
                                    item = topBudgetItem,
                                )
                            }
                        )
                    }
                    if (uiState.expenseSummaries.isNotEmpty()) {
                        item {
                            SummarySection(
                                title = "Expenses",
                                items = uiState.expenseSummaries,
                                onViewAllClick = {
                                    onEvent(
                                        HomeEvent.TypedTransactionsClicked(
                                            TransactionType.EXPENSE
                                        )
                                    )
                                },
                                onItemClick = { summaryItem ->
                                    onEvent(
                                        HomeEvent.CategorySummaryClicked(
                                            summaryItem
                                        )
                                    )
                                },
                                itemContent = { summaryItem ->
                                    CategorySummaryRow(
                                        categoryItem = summaryItem,
                                    )
                                }
                            )
                        }
                    }
                    if (uiState.incomeSummaries.isNotEmpty()) {
                        item {
                            SummarySection(
                                title = "Income",
                                items = uiState.incomeSummaries,
                                onViewAllClick = {
                                    onEvent(
                                        HomeEvent.TypedTransactionsClicked(
                                            TransactionType.INCOME
                                        )
                                    )
                                },
                                onItemClick = { summaryItem ->
                                    onEvent(
                                        HomeEvent.CategorySummaryClicked(
                                            summaryItem
                                        )
                                    )
                                },
                                itemContent = { summaryItem ->
                                    CategorySummaryRow(
                                        categoryItem = summaryItem,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { onEvent(HomeEvent.FabClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Transaction")
        }
    }
}