package com.rifqi.trackfunds.feature.reports.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.reports.ui.components.CustomDonutChart
import com.rifqi.trackfunds.feature.reports.ui.components.PeriodFilterBottomSheet
import com.rifqi.trackfunds.feature.reports.ui.event.ReportEvent
import com.rifqi.trackfunds.feature.reports.ui.state.ReportUiState
import com.rifqi.trackfunds.feature.reports.ui.viewmodel.ReportViewModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.time.Instant
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val activeBreakdownList by viewModel.activeBreakdownData.collectAsState()
    val datePickerState = rememberDateRangePickerState()

    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(ReportEvent.DatePickerDismissed) },
            confirmButton = {
                TextButton(onClick = {
                    val start = datePickerState.selectedStartDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val end = datePickerState.selectedEndDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    if (start != null && end != null) {
                        viewModel.onEvent(ReportEvent.DateRangeSelected(start, end))
                    }
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(ReportEvent.DatePickerDismissed) }) {
                    Text(
                        "Cancel"
                    )
                }
            }
        ) {
            DateRangePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Select Date Range",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                },
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    if (uiState.showPeriodSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ReportEvent.PeriodSheetDismissed) },
            sheetState = sheetState
        ) {
            PeriodFilterBottomSheet(
                selectedOption = uiState.selectedDateOption,
                onOptionClick = { option ->
                    viewModel.onEvent(ReportEvent.DateOptionSelected(option))
                },
                customStartDate = uiState.customStartDate,
                customEndDate = uiState.customEndDate,
                onCustomDateClick = {
                    viewModel.onEvent(ReportEvent.CustomDateRangeClicked)
                }
            )
        }
    }

    ReportContent(
        uiState = uiState,
        breakdownData = activeBreakdownList,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportContent(
    uiState: ReportUiState,
    breakdownData: List<CategorySpending>,
    onEvent: (ReportEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = {
                    Text(
                        "Financial reports",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                    ) {
                        DisplayIconFromResource(
                            identifier = "budgets",
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                actions = {
                    Text(
                        "June 2025",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                onEvent(ReportEvent.ChangePeriodClicked)
                            }
                    )
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = MaterialTheme.shapes.large
                            )
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {
                            Text(
                                "How is your financial allocation?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = uiState.activeBreakdownType == TransactionType.EXPENSE,
                                    onClick = {
                                        onEvent(
                                            ReportEvent.BreakdownTypeSelected(
                                                TransactionType.EXPENSE
                                            )
                                        )
                                    },
                                    label = { Text("Expense") },
                                    shape = MaterialTheme.shapes.large,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.secondary
                                    ),
                                )
                                FilterChip(
                                    selected = uiState.activeBreakdownType == TransactionType.INCOME,
                                    onClick = {
                                        onEvent(
                                            ReportEvent.BreakdownTypeSelected(
                                                TransactionType.INCOME
                                            )
                                        )
                                    },
                                    label = { Text("Income") },
                                    shape = MaterialTheme.shapes.large,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.secondary
                                    ),
                                )
                            }
                            if (breakdownData.isNotEmpty()) {
                                CustomDonutChart(data = breakdownData)
                            } else {
                                Text("Tidak ada data untuk periode ini.")
                            }
                        }
                    }

                    uiState.cashFlowSummary?.let { summary ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface
                                ),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Your cashflow profile",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(24.dp))

                                val incomeBrush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                    )
                                )
                                val expenseBrush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.error,
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                    )
                                )
                                val savingsBrush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.error,
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                                    )
                                )

                                ColumnChart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    data = remember(summary) {
                                        listOf(
                                            Bars(
                                                label = "",
                                                values = listOf(
                                                    Bars.Data(
                                                        label = "Income",
                                                        value = summary.totalIncome.toDouble(),
                                                        color = incomeBrush,
                                                    ),
                                                    Bars.Data(
                                                        label = "Expense",
                                                        value = summary.totalExpense.toDouble(),
                                                        color = expenseBrush
                                                    ),
                                                    Bars.Data(
                                                        label = "Savings",
                                                        value = 1_000_000.0,
                                                        color = savingsBrush
                                                    )
                                                ),
                                            )
                                        )
                                    },
                                    labelProperties = LabelProperties(
                                        enabled = true,
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 24.sp
                                        )
                                    ),
                                    barProperties = BarProperties(
                                        cornerRadius = Bars.Data.Radius.Rectangle(
                                            topRight = 6.dp,
                                            topLeft = 6.dp
                                        ),
                                        spacing = 30.dp,
                                        thickness = 70.dp
                                    ),
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    dividerProperties = DividerProperties(enabled = false),
                                    gridProperties = GridProperties(
                                        enabled = true,
                                        xAxisProperties = GridProperties.AxisProperties(
                                            thickness = (.4).dp,
                                        ),
                                        yAxisProperties = GridProperties.AxisProperties(
                                            enabled = false,
                                            thickness = (.1).dp,
                                        )
                                    ),
                                    indicatorProperties = HorizontalIndicatorProperties(
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 12.sp,
                                        ),
                                        contentBuilder = { value ->
                                            when {
                                                value >= 1_000_000 -> {
                                                    val millions = value / 1_000_000.0
                                                    "${
                                                        "%.1f".format(millions).replace(".0", "")
                                                    } jt"
                                                }

                                                value >= 1_000 -> {
                                                    "${(value / 1_000).toInt()} rb"
                                                }

                                                else -> {
                                                    value.toInt().toString()
                                                }
                                            }
                                        },
                                    ),
                                    labelHelperProperties = LabelHelperProperties(
                                        enabled = true,
                                        textStyle = TextStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 12.sp
                                        )
                                    ),
                                    popupProperties = PopupProperties(
                                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        contentBuilder = { _, _, value ->
                                            when {
                                                value >= 1_000_000 -> {
                                                    val millions = value / 1_000_000.0
                                                    "${
                                                        "%.1f".format(millions).replace(".0", "")
                                                    } jt"
                                                }

                                                value >= 1_000 -> {
                                                    "${(value / 1_000).toInt()} rb"
                                                }

                                                else -> {
                                                    value.toInt().toString()
                                                }
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}