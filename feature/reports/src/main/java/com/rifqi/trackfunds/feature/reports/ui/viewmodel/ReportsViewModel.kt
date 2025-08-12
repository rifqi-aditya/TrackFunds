package com.rifqi.trackfunds.feature.reports.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption
import com.rifqi.trackfunds.core.domain.reports.usecase.GetCashFlowReportUseCase
import com.rifqi.trackfunds.core.domain.reports.usecase.GetExpenseReportUseCase
import com.rifqi.trackfunds.core.domain.reports.usecase.GetIncomeReportUseCase
import com.rifqi.trackfunds.feature.reports.ui.event.ReportsEvent
import com.rifqi.trackfunds.feature.reports.ui.mapCashFlowReport
import com.rifqi.trackfunds.feature.reports.ui.mapExpenseReport
import com.rifqi.trackfunds.feature.reports.ui.mapIncomeReport
import com.rifqi.trackfunds.feature.reports.ui.sideeffect.ReportsSideEffect
import com.rifqi.trackfunds.feature.reports.ui.state.ActiveDialog
import com.rifqi.trackfunds.feature.reports.ui.state.ActiveSheet
import com.rifqi.trackfunds.feature.reports.ui.state.ReportsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getExpenseReportUseCase: GetExpenseReportUseCase,
    private val getIncomeReportUseCase: GetIncomeReportUseCase,
    private val getCashFlowReportUseCase: GetCashFlowReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ReportsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ReportsSideEffect> = _sideEffect.receiveAsFlow()

    private var expenseIncomeJob: Job? = null
    private var cashFlowJob: Job? = null

    // formatter label periode
    private val fullFmt = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
    private val monthFmt = DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
    private val dayMonthFmt = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    init {
        val (start, end) = resolveDateRange(DateRangeOption.THIS_MONTH)
        setState {
            it.copy(
                selectedDateOption = DateRangeOption.THIS_MONTH,
                formattedDateRange = makeDateFilterLabel(
                    DateRangeOption.THIS_MONTH,
                    it.customStartDate,
                    it.customEndDate
                ),
                customStartDate = null,
                customEndDate = null,
                isLoading = true
            )
        }
        loadExpenseAndIncome(start, end)
        loadCashFlow(_uiState.value.selectedCashFlowPeriod)
    }

    fun onEvent(event: ReportsEvent) {
        when (event) {
            is ReportsEvent.TabSelected -> setState { it.copy(selectedTabIndex = event.index) }

            ReportsEvent.FilterTriggerClicked -> {
                setState {
                    it.copy(
                        activeSheet = when (it.selectedTabIndex) {
                            0, 1 -> ActiveSheet.DATE_RANGE
                            else -> ActiveSheet.CASH_FLOW
                        }
                    )
                }
            }

            ReportsEvent.SheetDismissed ->
                setState { it.copy(activeSheet = null) }

            ReportsEvent.ExportClicked -> {
                // TODO: trigger export (share csv/pdf, dsb)
            }

            is ReportsEvent.PeriodOptionSelected -> {
                if (event.option == DateRangeOption.CUSTOM) {
                    setState { it.copy(activeSheet = null, activeDialog = ActiveDialog.DATE_RANGE) }
                } else {
                    val (start, end) = resolveDateRange(event.option)
                    setState {
                        it.copy(
                            selectedDateOption = event.option,
                            customStartDate = null,
                            customEndDate = null,
                            formattedDateRange = makeDateFilterLabel(
                                event.option,
                                null,
                                null
                            ),
                            activeSheet = null,
                            isLoading = true,
                            error = null
                        )
                    }
                    loadExpenseAndIncome(start, end)
                }
            }

            is ReportsEvent.DateRangeConfirmed -> {
                val (s, e) = normalizeRange(event.startDate, event.endDate)
                setState {
                    it.copy(
                        selectedDateOption = DateRangeOption.CUSTOM,
                        customStartDate = s,
                        customEndDate = e,
                        formattedDateRange = makeDateFilterLabel(
                            DateRangeOption.CUSTOM,
                            s,
                            e
                        ), // ← rentang tanggal
                        activeSheet = null,
                        activeDialog = null,
                        isLoading = true,
                        error = null
                    )
                }
                loadExpenseAndIncome(s, e)
            }


            is ReportsEvent.CashFlowPeriodSelected -> {
                setState {
                    it.copy(
                        selectedCashFlowPeriod = event.option,
                        activeSheet = null,
                        isLoading = true,
                        error = null
                    )
                }
                loadCashFlow(event.option)
            }

            ReportsEvent.DialogDismissed -> setState { it.copy(activeDialog = null) }
        }
    }

    // ---------------- Data loading ----------------

    private fun loadExpenseAndIncome(start: LocalDate, end: LocalDate) {
        expenseIncomeJob?.cancel()
        expenseIncomeJob = viewModelScope.launch {
            val expenseFlow = getExpenseReportUseCase(start, end)
            val incomeFlow = getIncomeReportUseCase(start, end)

            expenseFlow
                .combine(incomeFlow) { expenseDomain, incomeDomain ->
                    mapExpenseReport(expenseDomain) to mapIncomeReport(incomeDomain)
                }
                .onEach { (expenseUi, incomeUi) ->
                    setState {
                        it.copy(
                            expenseReportData = expenseUi,
                            incomeReportData = incomeUi,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .catch { e ->
                    setState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
                .collect()
        }
    }

    private fun loadCashFlow(option: CashFlowPeriodOption) {
        cashFlowJob?.cancel()
        cashFlowJob = viewModelScope.launch {
            getCashFlowReportUseCase(option)
                .map { mapCashFlowReport(it) }
                .onEach { ui ->
                    setState { it.copy(cashFlowReportData = ui, isLoading = false, error = null) }
                }
                .catch { e ->
                    setState { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
                }
                .collect()
        }
    }

    // ---------------- State helpers ----------------

    private inline fun setState(reducer: (ReportsUiState) -> ReportsUiState) {
        _uiState.update(reducer)
    }

    private fun resolveDateRange(
        option: DateRangeOption,
        today: LocalDate = LocalDate.now()
    ): Pair<LocalDate, LocalDate> = when (option) {
        DateRangeOption.THIS_MONTH -> {
            val ym = YearMonth.from(today)
            ym.atDay(1) to ym.atEndOfMonth()
        }

        DateRangeOption.LAST_MONTH -> {
            val ym = YearMonth.from(today).minusMonths(1)
            ym.atDay(1) to ym.atEndOfMonth()
        }

        DateRangeOption.THIS_YEAR -> {
            LocalDate.of(today.year, 1, 1) to LocalDate.of(today.year, 12, 31)
        }

        DateRangeOption.CUSTOM -> {
            val ym = YearMonth.from(today)
            ym.atDay(1) to ym.atEndOfMonth()
        }

        DateRangeOption.THIS_WEEK -> {
            val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
            val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY)
            startOfWeek to endOfWeek
        }

        DateRangeOption.ALL_TIME -> {
            LocalDate.of(2000, 1, 1) to today
        }
    }

    private fun normalizeRange(a: LocalDate, b: LocalDate): Pair<LocalDate, LocalDate> =
        if (a <= b) a to b else b to a

    private fun buildDateRangeLabel(start: LocalDate, end: LocalDate): String = when {
        start == end -> start.format(fullFmt)
        start.year == end.year && start.month == end.month ->
            "${start.dayOfMonth}–${end.dayOfMonth} ${end.format(monthFmt)}"

        start.year == end.year ->
            "${start.format(dayMonthFmt)} – ${end.format(fullFmt)}"

        else ->
            "${start.format(fullFmt)} – ${end.format(fullFmt)}"
    }

    private fun makeDateFilterLabel(
        option: DateRangeOption,
        customStart: LocalDate?,
        customEnd: LocalDate?
    ): String {
        return if (option == DateRangeOption.CUSTOM && customStart != null && customEnd != null) {
            buildDateRangeLabel(customStart, customEnd)
        } else {
            option.displayName // ← hanya nama opsi (mis. "This Month")
        }
    }
}