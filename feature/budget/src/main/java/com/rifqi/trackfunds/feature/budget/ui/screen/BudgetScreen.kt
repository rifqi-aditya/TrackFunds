package com.rifqi.trackfunds.feature.budget.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AddEditBudget
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.util.DisplayIconFromResource
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetCardItem
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetRingChart
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.model.BudgetTab
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetUiState
import com.rifqi.trackfunds.feature.budget.ui.viewmodel.BudgetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth


@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = hiltViewModel(),
    onNavigateToAddEditBudget: (AddEditBudget) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredBudgets by viewModel.filteredBudgets.collectAsState()

    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            if (screen is AddEditBudget) {
                onNavigateToAddEditBudget(screen)
            }
        }
    }

    if (uiState.showMonthPickerDialog) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.currentPeriod,
            onDismiss = { viewModel.onEvent(BudgetEvent.DialogDismissed) },
            onConfirm = { selectedYearMonth ->
                viewModel.onEvent(BudgetEvent.PeriodSelected(selectedYearMonth))
            }
        )
    }

    BudgetListContent(
        uiState = uiState,
        filteredBudgets = filteredBudgets,
        pagerState = pagerState,
        onEvent = viewModel::onEvent,
        scope = scope,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetListContent(
    uiState: BudgetUiState,
    filteredBudgets: List<BudgetItem>,
    pagerState: PagerState,
    scope: CoroutineScope,
    onEvent: (BudgetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = {
                    Text("Budgets", style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    DisplayIconFromResource(
                        identifier = "budgets",
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(32.dp)
                            .padding(end = 8.dp)
                    )
                },
                actions = {
                    Text(
                        uiState.currentPeriodDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onEvent(BudgetEvent.ChangePeriodClicked) }
                    )
                },
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(BudgetEvent.AddBudgetClicked) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val progress = if (uiState.totalBudgeted > BigDecimal.ZERO) {
                (uiState.totalSpent.toFloat() / uiState.totalBudgeted.toFloat())
            } else {
                0f
            }

            BudgetRingChart(
                progress = progress,
                remainingAmount = uiState.remainingOverall,
                totalAmount = uiState.totalBudgeted,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            width = tabPositions[pagerState.currentPage].contentWidth
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                BudgetTab.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            onEvent(BudgetEvent.TabSelected(tab))
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(if (tab == BudgetTab.YOUR_BUDGETS) "Your Budgets" else "Recurring") }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> { // Halaman "Your Budgets"
                        if (filteredBudgets.isEmpty() && uiState.categoriesWithBudget.isEmpty()) {
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
                                contentPadding = PaddingValues(vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Filter Chips
                                item {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(uiState.categoriesWithBudget) { category ->
                                            val isSelected =
                                                uiState.selectedCategoryFilterId == category.id
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = {
                                                    onEvent(
                                                        BudgetEvent.CategoryFilterClicked(
                                                            category.id
                                                        )
                                                    )
                                                },
                                                label = { Text(category.name) },
                                                shape = MaterialTheme.shapes.large,
                                                colors = FilterChipDefaults.filterChipColors(
                                                    selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                                                        alpha = 0.2f
                                                    ),
                                                    selectedLabelColor = MaterialTheme.colorScheme.secondary,
                                                    selectedLeadingIconColor = MaterialTheme.colorScheme.secondary
                                                ),

                                                // FIX: Lengkapi pemanggilan filterChipBorder dengan status saat ini
                                                border = FilterChipDefaults.filterChipBorder(
                                                    selected = isSelected, // <-- Beritahu status terpilih saat ini
                                                    enabled = true,        // <-- Kita asumsikan chip selalu bisa diklik

                                                    // Warna-warna yang sudah kita definisikan sebelumnya
                                                    selectedBorderColor = MaterialTheme.colorScheme.secondary,
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
                                items(
                                    items = filteredBudgets,
                                    key = { it.budgetId }
                                ) { budgetItem ->
                                    BudgetCardItem(
                                        item = budgetItem,
                                        onClick = {
                                            onEvent(
                                                BudgetEvent.EditBudgetClicked(
                                                    budgetItem.budgetId
                                                )
                                            )
                                        },
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }

                    1 -> { // Halaman "Recurring"
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Recurring budgets feature coming soon!")
                        }
                    }
                }
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
        "restaurant",
        BigDecimal("1000000"),
        BigDecimal("750000"),
        "2025-06"
    ),
    BudgetItem(
        "2",
        "c2",
        "Transportation",
        "commute",
        BigDecimal("400000"),
        BigDecimal("450000"),
        "2025-06"
    ),
    BudgetItem(
        "3",
        "c3",
        "Shopping",
        "shopping_bag",
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
    // Untuk preview, kita butuh PagerState dan CoroutineScope dummy
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    TrackFundsTheme {
        BudgetListContent(
            uiState = previewUiStateLoaded,
            filteredBudgets = previewBudgetItems,
            pagerState = pagerState,
            scope = scope,
            onEvent = {},
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(name = "Budget Screen - Empty", showBackground = true)
@Composable
private fun BudgetListContentEmptyPreview() {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    TrackFundsTheme {
        BudgetListContent(
            uiState = previewUiStateLoaded.copy(budgets = emptyList()),
            filteredBudgets = emptyList(),
            pagerState = pagerState,
            scope = scope,
            onEvent = {},
        )
    }
}