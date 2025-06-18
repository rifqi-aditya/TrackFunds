package com.rifqi.trackfunds.core.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.TransactionListItem
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.math.BigDecimal
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryContent(
    title: String,
    isLoading: Boolean,
    transactions: List<TransactionItem>,
    dateRange: String,
    error: String?,
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
                            title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            dateRange, // FIX 2: Gunakan parameter 'dateRange'
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
                            modifier = Modifier.size(18.dp)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // FIX 3: Gunakan parameter 'isLoading' dan 'error' untuk mengatur tampilan
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(
                            items = transactions, // FIX 4: Gunakan parameter 'transactions'
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
    }
}

// --- Preview ---

// Definisikan data dummy di sini untuk keperluan preview
private val DUMMY_ALL_TRANSACTIONS = listOf(
    TransactionItem(
        id = "1",
        note = "Monthly Salary",
        amount = BigDecimal("15000000"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now(),
        categoryId = "c1",
        categoryName = "Salary",
        iconIdentifier = "cash",
        accountId = "a1",
        accountName = "BCA Mobile"
    ),
    TransactionItem(
        id = "2",
        note = "Lunch with friends",
        amount = BigDecimal("150000"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().minusDays(1),
        categoryId = "c2",
        categoryName = "Food & Drink",
        iconIdentifier = "restaurant",
        accountId = "a2",
        accountName = "Cash Wallet"
    )
)

@Preview(showBackground = true, name = "Transactions Screen Light")
@Composable
private fun TransactionsContentPreview() {
    TrackFundsTheme {
        // FIX 5: Panggil dengan parameter individual, bukan UiState
        TransactionHistoryContent(
            title = "All Transactions",
            isLoading = false,
            transactions = DUMMY_ALL_TRANSACTIONS,
            dateRange = "01 - 30 June 2025",
            error = null,
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
        TransactionHistoryContent(
            title = "Expenses",
            isLoading = false,
            transactions = DUMMY_ALL_TRANSACTIONS,
            dateRange = "01 - 30 June 2025",
            error = null,
            onNavigateBack = {},
            onTransactionClick = {},
            onAddTransactionClick = {},
            onSearchClick = {},
            onCalendarClick = {}
        )
    }
}