package com.rifqi.trackfunds.feature.reports.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.theme.extendedColors
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.reports.ui.components.CashflowBarChart
import com.rifqi.trackfunds.feature.reports.ui.components.CashflowData
import com.rifqi.trackfunds.feature.reports.ui.components.CustomDonutChart
import com.rifqi.trackfunds.feature.reports.ui.event.ReportEvent
import com.rifqi.trackfunds.feature.reports.ui.state.ReportUiState
import com.rifqi.trackfunds.feature.reports.ui.viewmodel.ReportViewModel


@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeBreakdownList by viewModel.activeBreakdownData.collectAsState()

    // Tampilkan dialog pemilih bulan jika state-nya true
    if (uiState.showMonthPickerDialog) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.currentPeriod,
            onDismiss = { /* TODO: Kirim event dismiss */ },
            onConfirm = { selectedYearMonth ->
                viewModel.onEvent(ReportEvent.PeriodChanged(selectedYearMonth))
            }
        )
    }

    // Panggil UI murni
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
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = {
                    Text("Financial reports", style = MaterialTheme.typography.titleMedium)
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
                        "June 2025",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { }
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
                    // --- KARTU ANALISIS PENGELUARAN/PEMASUKAN ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "How is your financial allocation?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
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
                                        selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.2f
                                        ),
                                        selectedLabelColor = MaterialTheme.colorScheme.secondary,
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

                                val cashflowData = listOf(
                                    CashflowData(
                                        "Income",
                                        summary.totalIncome.toFloat(),
                                        MaterialTheme.extendedColors.income
                                    ),
                                    CashflowData(
                                        "Expenses",
                                        summary.totalExpense.toFloat(),
                                        MaterialTheme.extendedColors.expense
                                    ),
                                )

                                CashflowBarChart(data = cashflowData)
                            }
                        }
                    }
                }
            }
        }
    }
}