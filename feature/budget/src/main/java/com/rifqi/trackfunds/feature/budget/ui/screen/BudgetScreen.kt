package com.rifqi.trackfunds.feature.budget.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.BudgetRoutes
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetCardItem
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetMonthOverviewCard
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetRingChart
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetUiState
import com.rifqi.trackfunds.feature.budget.ui.viewmodel.BudgetViewModel
import java.math.BigDecimal
import java.time.YearMonth


@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = hiltViewModel(),
    onNavigateToAddEditBudget: (BudgetRoutes.AddEditBudget) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            if (screen is BudgetRoutes.AddEditBudget) {
                onNavigateToAddEditBudget(screen)
            }
        }
    }

    if (uiState.showMonthPickerDialog) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.currentPeriod,
            onDismiss = { viewModel.onEvent(BudgetEvent.MonthPickerDialogDismissed) },
            onConfirm = { selectedYearMonth ->
                viewModel.onEvent(BudgetEvent.PeriodSelected(selectedYearMonth))
            }
        )
    }

    BudgetListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListContent(
    uiState: BudgetUiState,
    onEvent: (BudgetEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopAppBar(
                    title = "Budgets",
                    actions = {
                        Column(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    onClick = { onEvent(BudgetEvent.ChangePeriodClicked) },
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    uiState.currentPeriodDisplay,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                )
                                DisplayIconFromResource(
                                    identifier = "arrow_down",
                                    contentDescription = "Change Period",
                                    modifier = Modifier
                                        .size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val progress = if (uiState.totalBudgeted > BigDecimal.ZERO) {
                    (uiState.totalSpent.toFloat() / uiState.totalBudgeted.toFloat())
                } else {
                    0f
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    BudgetRingChart(
                        progress = progress,
                        strokeWidth = 14.dp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Current Month Overview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        BudgetMonthOverviewCard(
                            title = "Total Budgeted",
                            amount = uiState.totalBudgeted,
                        )
                    }

                    item {
                        BudgetMonthOverviewCard(
                            title = "Total Spent",
                            amount = uiState.totalSpent,
                        )
                    }

                    item {
                        BudgetMonthOverviewCard(
                            title = "Remaining Budget",
                            amount = uiState.remainingOverall,
                        )
                    }
                }
                Text(
                    text = "Budget per Category",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                if (uiState.budgets.isEmpty() && uiState.categoriesWithBudget.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("No budgets set for this period. Tap '+' to add one.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.budgets,
                            key = { it.budgetId }
                        ) { budgetItem ->
                            BudgetCardItem(
                                item = budgetItem,
                                onClick = {

                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

            }
        }
        FloatingActionButton(
            onClick = { onEvent(BudgetEvent.AddBudgetClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
                Text(
                    "Add Budget",
                )
            }

        }
    }
}


// --- DUMMY DATA UNTUK KEPERLUAN PREVIEW ---

private val previewCategoriesForFilter = listOf(
    CategoryItem("c1", "Food & Drink", "restaurant", TransactionType.EXPENSE),
    CategoryItem("c2", "Transportation", "commute", TransactionType.EXPENSE),
    CategoryItem("c3", "Shopping", "shopping_bag", TransactionType.EXPENSE)
)

private val previewBudgetItems = listOf(
    BudgetItem(
        "1",
        "c1",
        "Food & Drink",
        "food_and_drink",
        BigDecimal("1000000"),
        BigDecimal("750000"),
        "2025-06"
    ),
    BudgetItem(
        "2",
        "c2",
        "Transportation",
        "transportation",
        BigDecimal("400000"),
        BigDecimal("450000"),
        "2025-06"
    ),
    BudgetItem(
        "3",
        "c3",
        "Shopping",
        "shopping",
        BigDecimal("1200000"),
        BigDecimal("500000"),
        "2025-06"
    )
)

private val previewUiStateLoaded = BudgetUiState(
    isLoading = false,
    budgets = previewBudgetItems,
    categoriesWithBudget = previewCategoriesForFilter,
    totalBudgeted = BigDecimal("2600000"),
    totalSpent = BigDecimal("1700000"),
    currentPeriod = YearMonth.now()
)

@OptIn(ExperimentalFoundationApi::class) // Tambahkan ini untuk PagerState
@Preview(name = "Budget Screen - Loaded", showBackground = true)
@Composable
private fun BudgetListContentLoadedPreview() {
    TrackFundsTheme {
        BudgetListContent(
            uiState = previewUiStateLoaded,
            onEvent = {},
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "Budget Screen - Empty", showBackground = true)
@Composable
private fun BudgetListContentEmptyPreview() {
    TrackFundsTheme {
        BudgetListContent(
            uiState = previewUiStateLoaded.copy(budgets = emptyList()),
            onEvent = {},
        )
    }
}