package com.rifqi.account.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.model.AccountTimelineUiState
import com.rifqi.account.ui.viewmodel.AccountTimelineViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


private fun formatTransactionTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
    return dateTime.format(formatter)
}

private fun formatTimelineDateHeader(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.ENGLISH)
    return date.format(formatter)
}


// --- Komponen UI ---

@Composable
fun TimelineHeader(
    accountName: String,
    balance: BigDecimal,
    onTransferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            accountName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatCurrency(balance),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Total Account Balance",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onTransferClick,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.CompareArrows,
                contentDescription = "Transfer",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Transfer")
        }
    }
}

@Composable
fun TransactionTimelineItem(
    transaction: TransactionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayIconFromResource(
            identifier = transaction.categoryIconIdentifier,
            contentDescription = transaction.categoryName,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.note.ifEmpty { transaction.categoryName },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatTransactionTime(transaction.date),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formatCurrency(transaction.amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == TransactionType.EXPENSE) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun DateHeader(date: LocalDate) {
    Text(
        text = formatTimelineDateHeader(date),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

// --- Layar Utama (Stateless) ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccountTimelineContent(
    uiState: AccountTimelineUiState,
    onNavigateBack: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onAddTransactionClick: () -> Unit,
    onTransferClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = { Text("Timeline") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = "Back")
                    }
                },
                actions = {
                    // Ikon seperti di gambar referensi (misalnya untuk filter/tampilan)
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Filled.Tune, contentDescription = "Filter")
                    }
                }
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
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                // Header utama dengan saldo dan tombol transfer
                item {
                    TimelineHeader(
                        accountName = uiState.accountName,
                        balance = uiState.currentBalance,
                        onTransferClick = onTransferClick
                    )
                }

                // Iterasi melalui Map untuk menampilkan header tanggal dan itemnya
                uiState.groupedTransactions.forEach { (date, transactionsOnDate) ->
                    // Header tanggal yang "lengket"
                    stickyHeader {
                        DateHeader(date = date)
                    }
                    // Item transaksi untuk tanggal tersebut
                    items(
                        items = transactionsOnDate,
                        key = { it.id }
                    ) { transaction ->
                        TransactionTimelineItem(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}

// --- Layar Stateful (Container) ---
@Composable
fun AccountTimelineScreen(
    viewModel: AccountTimelineViewModel = hiltViewModel(),
    account: String,
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountTimelineContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onTransactionClick = onNavigateToTransactionDetail,
        onAddTransactionClick = onNavigateToAddTransaction,
        onTransferClick = { viewModel.onTransferClicked() }
    )
}

private val DUMMY_TIMELINE_TRANSACTIONS = listOf(
    TransactionItem(
        id = "t1",
        note = "Mbanking initial balance",
        amount = BigDecimal("210000.0"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1).withHour(9),
        categoryId = "init",
        categoryName = "Initial",
        categoryIconIdentifier = "cash",
        accountId = "acc2",
        accountName = "acc2"
    ),
    TransactionItem(
        id = "t2",
        note = "Uang Jajan",
        amount = BigDecimal("210000.0"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(56),
        categoryId = "food",
        categoryName = "Makanan",
        categoryIconIdentifier = "restaurant",
        accountId = "acc2",
        accountName = "acc2"
    ),
    TransactionItem(
        id = "t3",
        note = "Liquid vape",
        amount = BigDecimal("210000.0"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(55),
        categoryId = "shop",
        categoryName = "Belanja",
        categoryIconIdentifier = "shopping_cart",
        accountId = "acc2",
        accountName = "acc2"
    )
)

// --- Preview ---
@Preview(showBackground = true, name = "Account Timeline - Light")
@Composable
private fun AccountTimelineContentPreview() {
    TrackFundsTheme {
        val dummyState = AccountTimelineUiState(
            isLoading = false,
            accountName = "Mbanking",
            currentBalance = BigDecimal("210000.0"),
            groupedTransactions = DUMMY_TIMELINE_TRANSACTIONS.groupBy { it.date.toLocalDate() }
        )
        AccountTimelineContent(
            uiState = dummyState,
            onNavigateBack = {},
            onTransactionClick = {},
            onAddTransactionClick = {},
            onTransferClick = {}
        )
    }
}