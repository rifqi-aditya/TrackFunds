package com.rifqi.trackfunds.feature.home.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRange
import com.rifqi.trackfunds.core.ui.util.getCurrentMonthAndYear
import com.rifqi.trackfunds.feature.home.ui.components.BalanceCard
import com.rifqi.trackfunds.feature.home.ui.components.ChallengeNotificationCard
import com.rifqi.trackfunds.feature.home.ui.components.TransactionSection
import com.rifqi.trackfunds.feature.home.ui.model.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.DUMMY_HOME_SUMMARY_DATA
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
    onNavigateToAllTransactions: (transactionType: String) -> Unit,
    onNavigateToBalanceDetails: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onNavigateToAllTransactions = onNavigateToAllTransactions,
        onNavigateToBalanceDetails = onNavigateToBalanceDetails,
        onNavigateToNotifications = onNavigateToNotifications,
        onCalendarClick = viewModel::onDateRangeClicked,
        onBalanceCardClick = viewModel::onBalanceCardClicked
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
    onNavigateToBalanceDetails: () -> Unit,
    onNavigateToAllTransactions: (transactionType: String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onCalendarClick: () -> Unit,
    onBalanceCardClick: () -> Unit
) {
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
                            uiState.dateRangePeriod,
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
                        onClick = {
                            onNavigateToBalanceDetails // Panggil handler dari ViewModel
                            // onNavigateToBalanceDetails() // Navigasi bisa dipicu dari ViewModel atau dari sini
                        }
                    )
                }
                if (uiState.summary != null) {
                    item {
                        TransactionSection(
                            title = "Expenses",
                            items = uiState.summary.recentExpenses,
                            onViewAllClick = { onNavigateToAllTransactions("EXPENSE") },
                            onItemClick = { transactionItem ->
                                println("Expense item clicked: ${transactionItem.categoryName}")
                            }
                        )
                    }
                    item {
                        TransactionSection(
                            title = "Income",
                            items = uiState.summary.recentIncome,
                            onViewAllClick = { onNavigateToAllTransactions("INCOME") },
                            onItemClick = { transactionItem ->
                                println("Income item clicked: ${transactionItem.categoryName}")
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}


// --- Preview SEKARANG memanggil HomeScreenContent ---
@Preview(showBackground = true, name = "Home Screen Content - Light")
@Composable
fun HomeScreenContentPreview() {
    TrackFundsTheme(darkTheme = false) {
        val previewState = HomeUiState(
            isLoading = false,
            currentMonthAndYear = getCurrentMonthAndYear(),
            dateRangePeriod = getCurrentDateRange(),
            summary = DUMMY_HOME_SUMMARY_DATA, // Data dummy
            challengeMessage = "Your first challenge is complete!",
            error = null
        )
        HomeScreenContent(
            uiState = previewState,
            // Berikan lambda kosong untuk semua aksi karena ini hanya preview UI
            onNavigateToAllTransactions = {},
            onNavigateToBalanceDetails = {},
            onNavigateToNotifications = {},
            onCalendarClick = {},
            onBalanceCardClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Home Screen Content - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenContentDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        val previewState = HomeUiState(
            isLoading = false,
            currentMonthAndYear = getCurrentMonthAndYear(),
            dateRangePeriod = getCurrentDateRange(),
            summary = DUMMY_HOME_SUMMARY_DATA,
            challengeMessage = null, // Contoh tanpa challenge
            error = null
        )
        HomeScreenContent(
            uiState = previewState,
            onNavigateToAllTransactions = {},
            onNavigateToBalanceDetails = {},
            onNavigateToNotifications = {},
            onCalendarClick = {},
            onBalanceCardClick = {}
        )
    }
}