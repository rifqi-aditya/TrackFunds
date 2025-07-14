package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.TransactionRow
import com.rifqi.trackfunds.feature.transaction.ui.components.CompactOutlinedTextField
import com.rifqi.trackfunds.feature.transaction.ui.components.SummaryCard
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionListEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionListUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransactionListViewModel


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
        LazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            // Item 1: Kartu Ringkasan Saldo
            item {
                SummaryCard(
                    spendableBalance = uiState.spendableBalance,
                    totalIncome = uiState.totalIncome,
                    totalExpense = uiState.totalExpense,
                    totalSavings = uiState.totalSavings
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Item 2: SearchBar dan Tombol Filter
            item {
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
            }

            if (activeChips.isNotEmpty()) {
                item {
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
            }
            // Item 4: Daftar Transaksi
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (uiState.error != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
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
