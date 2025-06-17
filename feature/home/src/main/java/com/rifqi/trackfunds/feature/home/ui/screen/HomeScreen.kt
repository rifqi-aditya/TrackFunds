package com.rifqi.trackfunds.feature.home.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.util.formatDateRangeToString
import com.rifqi.trackfunds.feature.home.ui.components.BalanceCard
import com.rifqi.trackfunds.feature.home.ui.components.CategorySummaryRow
import com.rifqi.trackfunds.feature.home.ui.components.ChallengeNotificationCard
import com.rifqi.trackfunds.feature.home.ui.components.SummarySection
import com.rifqi.trackfunds.feature.home.ui.model.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.HomeViewModel


/**
 * Stateful Composable (Container)
 * - Bertanggung jawab untuk mendapatkan ViewModel dan state.
 * - Menghubungkan logika ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToNotifications: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToAllTransactions: () -> Unit,
    onNavigateToCategoryDetails: (categoryId: String, categoryName: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onNavigateToNotifications = onNavigateToNotifications,
        onNavigateToAddTransaction = onNavigateToAddTransaction,
        onNavigateToAllTransactions = onNavigateToAllTransactions,
        onNavigateToCategoryDetails = onNavigateToCategoryDetails,
        onCalendarClick = { },
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
    onNavigateToAllTransactions: () -> Unit,
    onNavigateToCategoryDetails: (categoryId: String, categoryName: String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onCalendarClick: () -> Unit,
) {
    val dateRangeString = formatDateRangeToString(uiState.dateRangePeriod)

    Scaffold(
        topBar = {
            AppTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onCalendarClick) {
                        Image(
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = "Pilih Periode",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = {
                    Column {
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
                    IconButton(onClick = onNavigateToNotifications) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_notification),
                            contentDescription = "Notifikasi",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToAddTransaction() }) {
                Icon(Icons.Rounded.Add, contentDescription = "Tambah Transaksi")
            }
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
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                uiState.challengeMessage?.let { message ->
                    item { ChallengeNotificationCard(message = message) }
                }
                item {
                    BalanceCard(
                        summary = uiState.summary,
                        onClick = onNavigateToAllTransactions
                    )
                }
                if (uiState.summary != null) {
                    item {
                        SummarySection(
                            title = "Expenses",
                            items = uiState.summary.expenseSummaries,
                            onViewAllClick = { },
                            onItemClick = { summaryItem ->
                                onNavigateToCategoryDetails(
                                    summaryItem.categoryId,
                                    summaryItem.categoryName
                                )
                            },
                            itemContent = { summaryItem ->
                                CategorySummaryRow(
                                    categoryItem = summaryItem,
                                )
                            }
                        )
                    }
                    item {
                        SummarySection(
                            title = "Income",
                            items = uiState.summary.incomeSummaries,
                            onViewAllClick = { },
                            onItemClick = { summaryItem -> /* ... */ },
                            itemContent = { summaryItem ->
                                CategorySummaryRow(
                                    categoryItem = summaryItem,
                                )
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}