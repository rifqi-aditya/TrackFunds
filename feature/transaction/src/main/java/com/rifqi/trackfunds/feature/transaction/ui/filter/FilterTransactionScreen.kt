package com.rifqi.trackfunds.feature.transaction.ui.filter

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.common.model.DateRangeOption
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.preview.DummyData
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.ChipData
import com.rifqi.trackfunds.feature.transaction.ui.components.FilterGroup
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
            AppTopAppBar(
                title = "Filter",
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                onClick = { onEvent(FilterEvent.ApplyFilterClicked) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                )

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

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Transaction Date",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
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
                                    selectedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                                    selectedLabelColor = MaterialTheme.colorScheme.surface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    selected = isSelected,
                                    enabled = true,

                                    selectedBorderColor = MaterialTheme.colorScheme.surface,
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


// --- FUNGSI PREVIEW ---
@Preview(showBackground = true, name = "Filter Screen")
@Composable
private fun FilterScreenPreview() {
    TrackFundsTheme {
        FilterScreenContent(
            uiState = FilterTransactionUiState(
                isLoading = false,
                allCategories = listOf(
                    DummyData.dummyCategory1,
                    DummyData.dummyCategory2
                ),
                allAccounts = listOf(
                    DummyData.dummyAccount1,
                    DummyData.dummyAccount2
                ),
                selectedCategoryIds = setOf("1"),
                selectedAccountIds = setOf("acc1")
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
