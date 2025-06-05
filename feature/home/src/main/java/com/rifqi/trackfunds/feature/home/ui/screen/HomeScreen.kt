package com.rifqi.trackfunds.feature.home.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.home.ui.components.BalanceCard
import com.rifqi.trackfunds.feature.home.ui.components.ChallengeNotificationCard
import com.rifqi.trackfunds.feature.home.ui.components.HomeTopAppBar
import com.rifqi.trackfunds.feature.home.ui.components.TransactionSection
import com.rifqi.trackfunds.feature.home.ui.model.HomeUiState
import com.rifqi.trackfunds.feature.home.ui.viewmodel.DUMMY_HOME_SUMMARY_DATA
import com.rifqi.trackfunds.feature.home.ui.viewmodel.HomeViewModel
import com.rifqi.trackfunds.feature.home.util.getCurrentDateRange
import com.rifqi.trackfunds.feature.home.util.getCurrentMonthAndYear


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAllTransactions: (transactionType: String) -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToBalanceDetails: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    uiStateOverride: HomeUiState? = null
) {
    val actualUiState by if (uiStateOverride != null) {
        remember { mutableStateOf(uiStateOverride) }
    } else {
        viewModel.uiState.collectAsState()
    }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                currentMonth = actualUiState.currentMonthAndYear,
                dateRange = actualUiState.dateRangePeriod,
                onCalendarClick = { viewModel.onDateRangeClicked() },
                onNotificationsClick = {
                    viewModel.onNotificationsClicked()
                    // onNavigateToNotifications() // Contoh jika navigasi dihandle di sini
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTransaction) {
                Icon(Icons.Filled.Add, "Add Transaction")
            }
        }
    ) { innerPadding ->
        if (actualUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (actualUiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Error: ${actualUiState.error}",
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
                actualUiState.challengeMessage?.let { message ->
                    item { ChallengeNotificationCard(message = message) }
                }
                item {
                    BalanceCard(
                        summary = actualUiState.summary,
                        onClick = {
                            viewModel.onBalanceCardClicked()
                            // onNavigateToBalanceDetails() // Panggil callback navigasi jika perlu
                        }
                    )
                }

                // Bagian Pengeluaran (Expenses)
                if (actualUiState.summary != null) {
                    item {
                        TransactionSection(
                            title = "Expenses",
                            items = actualUiState.summary?.recentExpenses,
                            onViewAllClick = { onNavigateToAllTransactions("EXPENSE") },
                            onItemClick = { transactionItem ->
                                // TODO: Navigasi ke detail transaksi expense
                                println("Expense item clicked: ${transactionItem.categoryName}")
                            }
                        )
                    }

                    // Bagian Pemasukan (Income)
                    item {
                        TransactionSection(
                            title = "Income",
                            items = actualUiState.summary?.recentIncome,
                            onViewAllClick = { onNavigateToAllTransactions("INCOME") },
                            onItemClick = { transactionItem ->
                                // TODO: Navigasi ke detail transaksi income
                                println("Income item clicked: ${transactionItem.categoryName}")
                            }
                        )
                    }
                }
                // Spacer agar FAB tidak menutupi konten terakhir
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen - Light")
@Composable
fun HomeScreenLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        // Buat state dummy untuk preview
        val previewState = HomeUiState(
            isLoading = false, // <-- PENTING: Set isLoading ke false
            currentMonthAndYear = getCurrentMonthAndYear(), // Gunakan utilitas format
            dateRangePeriod = getCurrentDateRange(),   // Gunakan utilitas format
            summary = DUMMY_HOME_SUMMARY_DATA,        // Data summary dummy Anda
            challengeMessage = "Your first challenge is complete!",
            error = null
        )
        HomeScreen(
            uiStateOverride = previewState, // <-- TERUSKAN STATE KE SINI
            onNavigateToAllTransactions = {},
            onNavigateToAddTransaction = {},
            onNavigateToBalanceDetails = {},
            onNavigateToNotifications = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Home Screen - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        val previewState = HomeUiState(
            isLoading = false,
            currentMonthAndYear = getCurrentMonthAndYear(),
            dateRangePeriod = getCurrentDateRange(),
            summary = DUMMY_HOME_SUMMARY_DATA,
            challengeMessage = null, // Contoh tanpa challenge
            error = null
        )
        HomeScreen(
            uiStateOverride = previewState,
            onNavigateToAllTransactions = {},
            onNavigateToAddTransaction = {},
            onNavigateToBalanceDetails = {},
            onNavigateToNotifications = {}
        )
    }
}