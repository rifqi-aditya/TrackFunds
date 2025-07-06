package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.FilterTransactions
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionListEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip
import com.rifqi.trackfunds.feature.transaction.ui.model.DateRangeOption
import com.rifqi.trackfunds.feature.transaction.ui.model.FilterChipType
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val getCategoriesByIdsUseCase: GetCategoriesByIdsUseCase,
    private val getAccountsByIdsUseCase: GetAccountsByIdsUseCase,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionListUiState())
    val uiState: StateFlow<TransactionListUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()


    val activeFilterChips: StateFlow<List<ActiveFilterChip>> =
        _uiState.map { it.activeFilter }
            .distinctUntilChanged()
            .transformLatest { filter ->
                emit(buildActiveFilterChips(filter))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        applyInitialDateFilter()

        viewModelScope.launch {
            _uiState
                .map { it.activeFilter }
                .distinctUntilChanged()
                .flatMapLatest { filter ->
                    getFilteredTransactionsUseCase(filter)
                }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { transactionList ->
                    val totalIncome = transactionList.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val totalExpense = transactionList.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = transactionList,
                            totalIncome = totalIncome,
                            totalExpense = totalExpense,
                            error = if (transactionList.isEmpty()) "No transactions found." else null
                        )
                    }

                }
        }
        observeFilterResult()
    }

    fun onEvent(event: TransactionListEvent) {
        when (event) {
            is TransactionListEvent.SearchQueryChanged -> {
                _uiState.update {
                    it.copy(activeFilter = it.activeFilter.copy(searchQuery = event.query))
                }
            }

            is TransactionListEvent.FilterApplied -> {
                _uiState.update { it.copy(activeFilter = event.newFilter) }
            }

            TransactionListEvent.FilterButtonClicked -> {
                viewModelScope.launch {
                    resultManager.setArgument(_uiState.value.activeFilter)

                    _navigationEvent.emit(FilterTransactions)
                }
            }

            is TransactionListEvent.RemoveFilterChip -> {
                removeFilter(event.chip)
            }

            is TransactionListEvent.TransactionClicked -> {}
        }
    }

    private fun observeFilterResult() {
        viewModelScope.launch {
            resultManager.result.collect { resultData ->
                if (resultData is TransactionFilter) {
                    onEvent(TransactionListEvent.FilterApplied(resultData))
                    resultManager.setResult(null)
                }
            }
        }
    }

    private fun applyInitialDateFilter() {
        val today = LocalDate.now()
        val initialFilter = TransactionFilter(
            startDate = today.minusMonths(1).plusDays(1),
            endDate = today
        )
        _uiState.update { it.copy(activeFilter = initialFilter) }
    }

    private suspend fun buildActiveFilterChips(filter: TransactionFilter): List<ActiveFilterChip> {
        val chips = mutableListOf<ActiveFilterChip>()
        val today = LocalDate.now()


        val dateLabel = when {
            filter.startDate == null && filter.endDate == null -> null // All Time, jangan tampilkan chip
            filter.startDate == today && filter.endDate == today -> DateRangeOption.TODAY.displayName
            filter.startDate == today.minusDays(6) && filter.endDate == today -> DateRangeOption.LAST_7_DAYS.displayName

            filter.startDate == today.minusMonths(1)
                .plusDays(1) && filter.endDate == today -> DateRangeOption.LAST_30_DAYS.displayName

            filter.startDate == today.minusMonths(3)
                .plusDays(1) && filter.endDate == today -> DateRangeOption.LAST_90_DAYS.displayName

            filter.startDate != null && filter.endDate != null -> {
                // Untuk rentang tanggal kustom
                val formatter = DateTimeFormatter.ofPattern("d MMM")
                "${filter.startDate!!.format(formatter)} - ${filter.endDate!!.format(formatter)}"
            }

            else -> null
        }

        if (dateLabel != null) {
            chips.add(ActiveFilterChip("DATE_RANGE", dateLabel, FilterChipType.DATE_RANGE))
        }

        // 2. Tambahkan chip untuk setiap akun yang difilter
        filter.accountIds?.let { ids ->
            if (ids.isNotEmpty()) {
                val accounts = getAccountsByIdsUseCase(ids)
                accounts.forEach { account ->
                    chips.add(ActiveFilterChip(account.id, account.name, FilterChipType.ACCOUNT))
                }
            }
        }

        // 3. Tambahkan chip untuk setiap kategori yang difilter
        filter.categoryIds?.let { ids ->
            val categories = getCategoriesByIdsUseCase(ids)
            categories.forEach { category ->
                chips.add(ActiveFilterChip(category.id, category.name, FilterChipType.CATEGORY))
            }
        }

        return chips
    }

    private fun removeFilter(chip: ActiveFilterChip) {
        val currentFilter = _uiState.value.activeFilter
        val newFilter = when (chip.type) {
            FilterChipType.ACCOUNT -> currentFilter.copy(
                accountIds = currentFilter.accountIds?.filterNot { it == chip.id }
            )

            FilterChipType.CATEGORY -> currentFilter.copy(
                categoryIds = currentFilter.categoryIds?.filterNot { it == chip.id }
            )
            // Menghapus chip tanggal akan mereset ke periode default
            FilterChipType.DATE_RANGE -> currentFilter.copy(
                startDate = null,
                endDate = null
            )
        }
        _uiState.update { it.copy(activeFilter = newFilter) }
    }
}