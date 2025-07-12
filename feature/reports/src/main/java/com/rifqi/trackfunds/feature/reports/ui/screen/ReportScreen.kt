package com.rifqi.trackfunds.feature.reports.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.reports.ui.components.CashflowProfileCard
import com.rifqi.trackfunds.feature.reports.ui.components.FinancialAllocationCard
import com.rifqi.trackfunds.feature.reports.ui.components.PeriodFilterBottomSheet
import com.rifqi.trackfunds.feature.reports.ui.event.ReportEvent
import com.rifqi.trackfunds.feature.reports.ui.state.ReportUiState
import com.rifqi.trackfunds.feature.reports.ui.viewmodel.ReportViewModel
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FinancialAllocationCard(
                        activeBreakdownType = uiState.activeBreakdownType,
                        breakdownData = breakdownData,
                        onBreakdownTypeSelected = { type ->
                            onEvent(ReportEvent.BreakdownTypeSelected(type))
                        },
                    )

                    uiState.cashFlowSummary?.let { summary ->
                        CashflowProfileCard(
                            summary = summary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}