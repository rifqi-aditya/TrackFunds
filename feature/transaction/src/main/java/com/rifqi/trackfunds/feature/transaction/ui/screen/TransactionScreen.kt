package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.TransactionRow
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.CompactOutlinedTextField
import com.rifqi.trackfunds.feature.transaction.ui.components.SummaryCard
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionListEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip
import com.rifqi.trackfunds.feature.transaction.ui.model.FilterChipType
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionListUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransactionListViewModel
import java.math.BigDecimal
import java.time.LocalDateTime


/**
 * Stateful Composable (Container)
 * - Menghubungkan ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun TransactionScreen(
    viewModel: TransactionListViewModel = hiltViewModel(),
    onNavigate: (AppScreen) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeChips by viewModel.activeFilterChips.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            onNavigate(screen)
        }
    }

    TransactionScreenContent(
        uiState = uiState,
        activeChips = activeChips,
        onEvent = viewModel::onEvent,
    )
}

/**
 * Stateless Composable (Presentational)
 * - Hanya menerima state dan lambda.
 * - Sangat mudah untuk di-preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreenContent(
    uiState: TransactionListUiState,
    activeChips: List<ActiveFilterChip>,
    onEvent: (TransactionListEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Transactions",
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SummaryCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                spendableBalance = uiState.spendableBalance,
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                totalSavings = uiState.totalSavings,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactOutlinedTextField(
                    value = uiState.activeFilter.searchQuery,
                    onValueChange = { query ->
                        onEvent(TransactionListEvent.SearchQueryChanged(query))
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            "Search transactions...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                        )
                    }
                )

                OutlinedButton(
                    onClick = { onEvent(TransactionListEvent.FilterButtonClicked) },
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Icon(
                        Icons.Default.FilterAlt,
                        contentDescription = "Filter",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Filter",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }


            if (activeChips.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(activeChips, key = { it.id + it.label }) { chip ->
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(chip.label)
                                    IconButton(
                                        onClick = {
                                            onEvent(
                                                TransactionListEvent.RemoveFilterChip(
                                                    chip
                                                )
                                            )
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Hapus filter ${chip.label}",
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            },
                            shape = MaterialTheme.shapes.large,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.background,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                selected = true,
                                enabled = true,

                                selectedBorderColor = MaterialTheme.colorScheme.primary,
                                selectedBorderWidth = 1.5.dp,
                                borderColor = MaterialTheme.colorScheme.outline,
                                borderWidth = 1.dp
                            )
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
                }

            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.transactions) { transaction ->
                        TransactionRow(
                            item = transaction,
                            onClick = { onEvent(TransactionListEvent.TransactionClicked(transaction.id)) }
                        )
                    }
                }
            }
        }
    }
}

private val previewTransactions = listOf(
    Transaction(
        id = "1",
        amount = BigDecimal("75000"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now(),
        category = CategoryItem(
            id = "cat1",
            name = "Makan & Minum",
            iconIdentifier = "restaurant",
            type = TransactionType.EXPENSE
        ),
        account = AccountItem(
            id = "acc1",
            name = "Cash",
            iconIdentifier = "wallet",
            balance = BigDecimal("1000000")
        ),
        description = "Shell",
    ),
)

private val previewActiveChips = listOf(
    ActiveFilterChip("DATE_RANGE", "Bulan Ini", FilterChipType.DATE_RANGE),
    ActiveFilterChip("cat1", "Makan & Minum", FilterChipType.CATEGORY)
)

private val previewLoadedState = TransactionListUiState(
    isLoading = false,
    activeFilter = TransactionFilter(searchQuery = "Kopi"),
    transactions = previewTransactions,
    totalIncome = BigDecimal("7500000"),
    totalExpense = BigDecimal("225000")
)

@Preview(showBackground = true, name = "Transaction Screen - Loaded")
@Composable
private fun TransactionScreenContentLoadedPreview() {
    TrackFundsTheme {
        TransactionScreenContent(
            uiState = previewLoadedState,
            activeChips = previewActiveChips,
            onEvent = {}
        )
    }
}
