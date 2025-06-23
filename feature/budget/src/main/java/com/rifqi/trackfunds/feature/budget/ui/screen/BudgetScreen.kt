package com.rifqi.trackfunds.feature.budget.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.navigation.api.AddEditBudget
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetItemRow
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetListEvent
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetListUiState
import com.rifqi.trackfunds.feature.budget.ui.viewmodel.BudgetListViewModel
import java.math.BigDecimal


@Composable
fun BudgetScreen(
    viewModel: BudgetListViewModel = hiltViewModel(),
    onNavigateToAddEditBudget: (AddEditBudget) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMonthPickerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            // Cek apakah sinyalnya adalah untuk AddEditBudget
            if (screen is AddEditBudget) {
                onNavigateToAddEditBudget(screen)
            }
        }
    }

    // Tampilkan dialog pemilih bulan jika state-nya true
    if (showMonthPickerDialog) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.currentPeriod,
            onDismiss = { showMonthPickerDialog = false },
            onConfirm = { selectedYearMonth ->
                viewModel.onEvent(BudgetListEvent.ChangePeriod(selectedYearMonth))
                showMonthPickerDialog = false
            }
        )
    }

    // Panggil UI murni dengan state dan event handler
    BudgetListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onDateRangeSelectorClicked = { showMonthPickerDialog = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListContent(
    uiState: BudgetListUiState,
    onEvent: (BudgetListEvent) -> Unit,
    onDateRangeSelectorClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onDateRangeSelectorClicked) {
                        Image(
                            painter = painterResource(R.drawable.ic_calendar),
                            contentDescription = "Pilih Periode",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                title = {
                    Text(
                        uiState.currentPeriodDisplay,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.clickable(
                            onClick = onDateRangeSelectorClicked
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(BudgetListEvent.AddBudgetClicked) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            } else if (uiState.budgets.isEmpty()) {
                Text(
                    text = "No budgets set for this period. Tap '+' to add one.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // TODO: Tampilkan kartu ringkasan total di sini
                    items(
                        items = uiState.budgets,
                        key = { it.budgetId }
                    ) { budgetItem ->
                        BudgetItemRow(
                            item = budgetItem,
                            onClick = { onEvent(BudgetListEvent.EditBudgetClicked(budgetItem.budgetId)) }
                        )
                    }
                }
            }
        }
    }
}

private val previewBudgets = listOf(
    BudgetItem(
        "1",
        "c1",
        "Food & Drink",
        "restaurant",
        BigDecimal("1000000"),
        BigDecimal("750000"),
        "2025-06"
    ),
    BudgetItem(
        "2",
        "c2",
        "Transportation",
        "car",
        BigDecimal("400000"),
        BigDecimal("450000"),
        "2025-06"
    ),
    BudgetItem(
        "3",
        "c3",
        "Entertainment",
        "movie",
        BigDecimal("500000"),
        BigDecimal("100000"),
        "2025-06"
    )
)

@Preview(showBackground = true, name = "Budget Content - Loaded")
@Composable
private fun BudgetListContentLoadedPreview() {
    TrackFundsTheme {
        BudgetListContent(
            uiState = BudgetListUiState(isLoading = false, budgets = previewBudgets),
            onEvent = {},
            onDateRangeSelectorClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Budget Content - Empty")
@Composable
private fun BudgetListContentEmptyPreview() {
    TrackFundsTheme {
        BudgetListContent(
            uiState = BudgetListUiState(isLoading = false, budgets = emptyList()),
            onEvent = {},
            onDateRangeSelectorClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Budget Content - Loading")
@Composable
private fun BudgetListContentLoadingPreview() {
    TrackFundsTheme {
        BudgetListContent(
            uiState = BudgetListUiState(isLoading = true),
            onEvent = {},
            onDateRangeSelectorClicked = {}
        )
    }
}