package com.rifqi.trackfunds.feature.transaction.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.SummaryHeaderCard
import com.rifqi.trackfunds.feature.transaction.ui.components.TransactionListItem
import com.rifqi.trackfunds.feature.transaction.ui.model.TransactionListUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransactionListViewModel
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.dummyTransactionList
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun formatTransactionDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy hh:mm a", Locale.ENGLISH)
    return dateTime.format(formatter)
}

// --- Layar Stateful (Container) ---
@Composable
fun TransactionsScreen(
    viewModel: TransactionListViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (transactionId: String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TransactionsContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onTransactionClick = onNavigateToTransactionDetail,
        onAddTransactionClick = onNavigateToAddTransaction,
        onSearchClick = { /* TODO: Panggil fungsi ViewModel untuk search */ },
        onCalendarClick = { /* TODO: Panggil fungsi ViewModel untuk ganti tanggal */ }
    )
}


// --- Layar Utama (Stateless) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsContent(
    uiState: TransactionListUiState,
    onNavigateBack: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onAddTransactionClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCalendarClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    Column {
                        Text(
                            "Transactions",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            uiState.dateRange,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onCalendarClick) {
                        Image(
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = "Filter by Date",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp) // Jarak kecil antar Card/Divider
            ) {
                item {
                    SummaryHeaderCard(
                        elapsedAmount = uiState.elapsedAmount,
                        upcomingAmount = uiState.upcomingAmount
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(
                    items = uiState.transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionListItem(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction.id) }
                    )
                }
            }
        }
    }
}


// --- Preview ---
@Preview(showBackground = true, name = "Transactions Screen Light")
@Composable
private fun TransactionsContentPreview() {
    TrackFundsTheme {
        val dummyState = TransactionListUiState(
            isLoading = false,
            dateRange = "01 Jun 25 - 30 Jun 25",
            elapsedAmount = BigDecimal("50000.0"),
            upcomingAmount = BigDecimal.ZERO,
            transactions = dummyTransactionList
        )
        TransactionsContent(
            uiState = dummyState,
            onNavigateBack = {},
            onTransactionClick = {},
            onAddTransactionClick = {},
            onSearchClick = {},
            onCalendarClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Transactions Screen Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TransactionsContentDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        val dummyState = TransactionListUiState(
            isLoading = false,
            dateRange = "01 Jun 25 - 30 Jun 25",
            elapsedAmount = BigDecimal("50000.0"),
            upcomingAmount = BigDecimal.ZERO,
            transactions = dummyTransactionList
        )
        TransactionsContent(
            uiState = dummyState,
            onNavigateBack = {},
            onTransactionClick = {},
            onAddTransactionClick = {},
            onSearchClick = {},
            onCalendarClick = {}
        )
    }
}