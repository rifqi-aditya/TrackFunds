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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.feature.transaction.ui.components.CompactOutlinedTextField
import com.rifqi.trackfunds.core.ui.components.TransactionRow
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionListEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionListUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransactionListViewModel
import java.math.BigDecimal
import java.time.LocalDate
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
            // TODO: Tambahkan TopAppBar jika diperlukan
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Item 1: Kartu Ringkasan Saldo
            item {
                // TODO: Buat atau panggil komponen BalanceSummaryCard
                // Contoh: BalanceSummaryCard(income = uiState.totalIncome, expense = uiState.totalExpense)
                Spacer(modifier = Modifier.height(16.dp))
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
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )

                    OutlinedButton(
                        onClick = { onEvent(TransactionListEvent.FilterButtonClicked) },
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ),
                    ) {
                        Icon(
                            Icons.Default.FilterAlt,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Filter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primaryContainer
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
                                    selectedContainerColor = Color.Transparent,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    selected = true,
                                    enabled = true,

                                    selectedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedBorderWidth = 1.5.dp,
                                    borderColor = MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.5f
                                    ),
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

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewTransactions = listOf(
    TransactionItem(
        id = "1",
        amount = BigDecimal("50000"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now(),
        description = "Kopi Pagi",
        categoryName = "Makan & Minum",
        accountName = "Dompet Digital",
        categoryIconIdentifier = "restaurant",
        categoryId = "cat3",
        accountId = "acc3",
        accountIconIdentifier = "bank_account",
        transferPairId = "trf1"
    ),
    TransactionItem(
        id = "2",
        amount = BigDecimal("7500000"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1),
        description = "Gaji Bulanan",
        categoryName = "Gaji",
        accountName = "Rekening Utama",
        categoryIconIdentifier = "salary",
        categoryId = "cat1",
        accountId = "acc1",
        accountIconIdentifier = "bank_account",
        transferPairId = "trf2"
    ),
    TransactionItem(
        id = "3",
        amount = BigDecimal("150000"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().minusDays(2),
        description = "Bensin Motor",
        categoryName = "Transportasi",
        accountName = "Uang Tunai",
        categoryIconIdentifier = "transportation",
        categoryId = "cat2",
        accountId = "acc2",
        accountIconIdentifier = "bank_account",
        transferPairId = "trf1",
    )
)

private val previewUiStateLoaded = TransactionListUiState(
    isLoading = false,
    activeFilter = TransactionFilter(
        searchQuery = "",
        categoryIds = listOf("cat1", "cat2"),
        startDate = LocalDate.now(),
        endDate = LocalDate.now(),
    ),
    transactions = previewTransactions,
    totalIncome = BigDecimal("7500000"),
    totalExpense = BigDecimal("225000")
)

// --- FUNGSI-FUNGSI PREVIEW ---
//
//@Preview(showBackground = true, name = "Transaction Screen - Loaded")
//@Composable
//private fun TransactionScreenContentLoadedPreview() {
//    TrackFundsTheme {
//        TransactionScreenContent(
//            uiState = previewUiStateLoaded,
//            onEvent = {},
//            activeChips = TODO(),
//            onTransactionClick = TODO(),
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Transaction Screen - Empty")
//@Composable
//private fun TransactionScreenContentEmptyPreview() {
//    TrackFundsTheme {
//        TransactionScreenContent(
//            uiState = previewUiStateLoaded.copy(
//                transactions = emptyList(),
//                error = "Tidak ada transaksi ditemukan." // Contoh pesan error saat kosong
//            ),
//            onEvent = {},
//            onNavigateToFilter = {},
//            onTransactionClick = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Transaction Screen - Loading")
//@Composable
//private fun TransactionScreenContentLoadingPreview() {
//    TrackFundsTheme {
//        TransactionScreenContent(
//            uiState = TransactionListUiState(isLoading = true),
//            onEvent = {},
//            onNavigateToFilter = {},
//            onTransactionClick = {}
//        )
//    }
//}
