package com.rifqi.trackfunds.feature.reports.ui.screen


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.EmptyState
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.reports.ui.components.CashFlowPeriodSheet
import com.rifqi.trackfunds.feature.reports.ui.components.CashFlowRow
import com.rifqi.trackfunds.feature.reports.ui.components.CashflowBarChart
import com.rifqi.trackfunds.feature.reports.ui.components.CategoryRow
import com.rifqi.trackfunds.feature.reports.ui.components.CustomDateRangePickerDialog
import com.rifqi.trackfunds.feature.reports.ui.components.CustomDonutChart
import com.rifqi.trackfunds.feature.reports.ui.components.DateRangeSheet
import com.rifqi.trackfunds.feature.reports.ui.event.ReportsEvent
import com.rifqi.trackfunds.feature.reports.ui.event.ReportsEvent.CashFlowPeriodSelected
import com.rifqi.trackfunds.feature.reports.ui.event.ReportsEvent.PeriodOptionSelected
import com.rifqi.trackfunds.feature.reports.ui.sideeffect.ReportsSideEffect
import com.rifqi.trackfunds.feature.reports.ui.state.ActiveDialog
import com.rifqi.trackfunds.feature.reports.ui.state.ActiveSheet
import com.rifqi.trackfunds.feature.reports.ui.state.CashFlowReportData
import com.rifqi.trackfunds.feature.reports.ui.state.ExpenseReportData
import com.rifqi.trackfunds.feature.reports.ui.state.IncomeReportData
import com.rifqi.trackfunds.feature.reports.ui.state.ReportsUiState
import com.rifqi.trackfunds.feature.reports.ui.viewmodel.ReportsViewModel
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dateRangePickerState = rememberDateRangePickerState()

    val zone = remember { java.time.ZoneId.systemDefault() }

    if (uiState.activeDialog == ActiveDialog.DATE_RANGE) {
        CustomDateRangePickerDialog(
            state = dateRangePickerState,
            onDismiss = { viewModel.onEvent(ReportsEvent.DialogDismissed) },
            onConfirm = { startMillis, endMillis ->
                val startDate = Instant.ofEpochMilli(startMillis)
                    .atZone(zone).toLocalDate()
                val endDate = Instant.ofEpochMilli(endMillis)
                    .atZone(zone).toLocalDate()
                viewModel.onEvent(ReportsEvent.DateRangeConfirmed(startDate, endDate))
            }
        )
    }

    if (uiState.activeSheet != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { viewModel.onEvent(ReportsEvent.SheetDismissed) }
        ) {
            when (uiState.activeSheet) {
                ActiveSheet.DATE_RANGE -> {
                    DateRangeSheet(
                        selected = uiState.selectedDateOption,
                        customStart = uiState.customStartDate,
                        customEnd = uiState.customEndDate,
                        onPick = { viewModel.onEvent(PeriodOptionSelected(it)) },
                        onClose = { viewModel.onEvent(ReportsEvent.SheetDismissed) }
                    )
                }

                ActiveSheet.CASH_FLOW -> {
                    CashFlowPeriodSheet(
                        selected = uiState.selectedCashFlowPeriod,
                        onPick = { viewModel.onEvent(CashFlowPeriodSelected(it)) },
                        onClose = { viewModel.onEvent(ReportsEvent.SheetDismissed) }
                    )
                }

                null -> TODO()
            }
        }
    }



    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ReportsSideEffect.ShowDateRangePicker -> {

                }
            }
        }
    }

    ReportsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsContent(
    uiState: ReportsUiState,
    onEvent: (ReportsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Reports",
                actions = {
                    IconButton(onClick = { onEvent(ReportsEvent.ExportClicked) }) {
                        Icon(Icons.Default.FileUpload, contentDescription = "Export Data")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Tabs
            TabRow(selectedTabIndex = uiState.selectedTabIndex) {
                listOf("Expense", "Income", "Cash Flow").forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTabIndex == index,
                        onClick = { onEvent(ReportsEvent.TabSelected(index)) }, // Kirim event
                        text = { Text(title) }
                    )
                }
            }

            AnimatedContent(
                targetState = uiState.selectedTabIndex,
                label = "FilterAnimation"
            ) { tabIndex ->
                val (label, icon) = when (tabIndex) {
                    0, 1 -> uiState.formattedDateRange to Icons.Default.CalendarToday
                    else -> uiState.selectedCashFlowPeriod.displayName to Icons.Default.TrendingUp
                }
                FilterTriggerCard(
                    label = label,
                    leadingIcon = icon,
                    onClick = { onEvent(ReportsEvent.FilterTriggerClicked) }
                )
            }


            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Konten dinamis berdasarkan state
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.error != null) {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    // Ganti konten berdasarkan tab yang aktif
                    when (uiState.selectedTabIndex) {
                        0 -> ExpenseReportContent(data = uiState.expenseReportData)
                        1 -> IncomeReportContent(data = uiState.incomeReportData)
                        2 -> CashFlowReportContent(data = uiState.cashFlowReportData)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterTriggerCard(
    label: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(leadingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ExpenseReportContent(data: ExpenseReportData) {
    if (data.categorySummaries.isEmpty()) {
        EmptyState(title = "No Expense Data", message = "There are no expenses in this period.")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Bungkus Chart dalam Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomDonutChart(data = data.categorySummaries)
                }
            }
        }

        // --- 2. Bungkus Daftar Rincian dalam Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.categorySummaries.forEachIndexed { index, category ->
                        CategoryRow(summary = category)
                        if (index < data.categorySummaries.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = DividerDefaults.Thickness,
                                color = DividerDefaults.color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncomeReportContent(data: IncomeReportData) {
    if (data.categorySummaries.isEmpty()) {
        EmptyState(title = "No Income Data", message = "There are no income in this period.")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Bungkus Chart dalam Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomDonutChart(data = data.categorySummaries)
                }
            }
        }

        // --- 2. Bungkus Daftar Rincian dalam Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.categorySummaries.forEachIndexed { index, category ->
                        CategoryRow(summary = category)
                        if (index < data.categorySummaries.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 8.dp),
                                thickness = DividerDefaults.Thickness,
                                color = DividerDefaults.color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CashFlowReportContent(data: CashFlowReportData) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Item untuk grafik bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                CashflowBarChart(monthlyFlows = data.chartItems)
            }
        }

        item {
            Text(
                "Monthly Breakdown",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Daftar rincian per bulan
        items(data.listItems) { listItem ->
            CashFlowRow(item = listItem)
        }
    }
}

// --- 5. Preview ---
@Preview(showBackground = true)
@Composable
private fun ReportsScreenPreview() {
    TrackFundsTheme {
    }
}