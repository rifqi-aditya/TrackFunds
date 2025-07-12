package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.ChipData
import com.rifqi.trackfunds.feature.transaction.ui.components.FilterGroup
import com.rifqi.trackfunds.feature.transaction.ui.event.FilterEvent
import com.rifqi.trackfunds.core.common.model.DateRangeOption
import com.rifqi.trackfunds.feature.transaction.ui.state.FilterTransactionUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.FilterTransactionViewModel
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

@Composable
fun FilterScreen(
    viewModel: FilterTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNavigateBack()
        }
    }

    FilterScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreenContent(
    uiState: FilterTransactionUiState,
    onEvent: (FilterEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { /* ... */ } })
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { onEvent(FilterEvent.ApplyFilterClicked) }
            ) {
                Text("Apply Filter")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FilterGroup(
                    title = "Category",
                    chips = uiState.allCategories.map {
                        ChipData(
                            it.id,
                            it.name,
                            it.iconIdentifier,
                            uiState.selectedCategoryIds.contains(it.id)
                        )
                    },
                    onChipClick = { onEvent(FilterEvent.CategoryToggled(it)) }
                )
            }

            // Bagian Sumber Akun
            item {
                FilterGroup(
                    title = "Account Source",
                    chips = uiState.allAccounts.map {
                        ChipData(
                            it.id,
                            it.name,
                            it.iconIdentifier,
                            uiState.selectedAccountIds.contains(it.id)
                        )
                    },
                    onChipClick = { onEvent(FilterEvent.AccountToggled(it)) }
                )
            }

            // Bagian Tanggal Transaksi
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Transaction Date",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { onEvent(FilterEvent.ManualDateSelectionClicked) }) {
                        Text("Custom Date")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DateRangeOption.entries.filter { it != DateRangeOption.CUSTOM }
                        .forEach { option ->
                            val isSelected = uiState.selectedDateOption == option
                            FilterChip(
                                selected = isSelected,
                                onClick = { onEvent(FilterEvent.DateOptionSelected(option)) },
                                label = { Text(option.displayName) },
                                shape = MaterialTheme.shapes.large,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color.Transparent,
                                    selectedLabelColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    selected = isSelected,
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

                    // Tampilkan chip untuk tanggal kustom jika aktif
                    if (uiState.selectedDateOption == DateRangeOption.CUSTOM &&
                        uiState.customStartDate != null && uiState.customEndDate != null
                    ) {
                        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
                        val label = "${uiState.customStartDate.format(formatter)} - ${
                            uiState.customEndDate.format(formatter)
                        }"
                        FilterChip(
                            selected = true,
                            onClick = { onEvent(FilterEvent.ManualDateSelectionClicked) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    }
}

// --- DATA DUMMY UNTUK PREVIEW ---
private val previewCategories = listOf(
    CategoryItem(
        id = "1",
        name = "Makan & Minum",
        iconIdentifier = "restaurant",
        type = TransactionType.EXPENSE
    ),
    CategoryItem(
        id = "2",
        name = "Transportasi",
        iconIdentifier = "commute",
        type = TransactionType.EXPENSE
    ),
    CategoryItem(
        id = "3",
        name = "Belanja",
        iconIdentifier = "shopping_bag",
        type = TransactionType.EXPENSE
    ),
    CategoryItem(id = "4", name = "Gaji", iconIdentifier = "salary", type = TransactionType.INCOME)
)

private val previewAccounts = listOf(
    AccountItem(
        id = "acc1",
        name = "KAS",
        iconIdentifier = "wallet_account",
        balance = BigDecimal.ZERO
    ),
    AccountItem(
        id = "acc2",
        name = "BNI Mobile",
        iconIdentifier = "bank_account",
        balance = BigDecimal.ZERO
    )
)

// --- FUNGSI PREVIEW ---
@Preview(showBackground = true, name = "Filter Screen")
@Composable
private fun FilterScreenPreview() {
    TrackFundsTheme {
        FilterScreenContent(
            uiState = FilterTransactionUiState(
                isLoading = false,
                allCategories = previewCategories,
                allAccounts = previewAccounts,
                // Simulasikan kategori "Makan & Minum" dan akun "KAS" sudah dipilih
                selectedCategoryIds = setOf("1"),
                selectedAccountIds = setOf("acc1")
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
