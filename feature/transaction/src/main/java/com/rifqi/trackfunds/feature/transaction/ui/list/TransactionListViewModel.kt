package com.rifqi.trackfunds.feature.transaction.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsByIdsUseCase
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoriesByIdsUseCase
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip
import com.rifqi.trackfunds.feature.transaction.ui.model.FilterChipType
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

    private val transactionsFlow = _uiState
        .map { it.activeFilter }
        .distinctUntilChanged()
        .flatMapLatest { filter ->
            getFilteredTransactionsUseCase(filter)
        }

    val activeFilterChips: StateFlow<List<ActiveFilterChip>> =
        _uiState.map { it.activeFilter }
            .distinctUntilChanged()
            .transformLatest { filter -> emit(buildActiveFilterChips(filter)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        applyInitialDateFilter()
        observeDataChanges()
        observeFilterResult()
    }

    private fun observeDataChanges() {
        transactionsFlow
            .onEach { transactionList ->
                val totalIncome =
                    transactionList.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val totalExpense = transactionList.filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                val net = totalIncome - totalExpense

                val (emptyTitle, emptyMessage) = if (transactionList.isEmpty()) {
                    determineEmptyState(_uiState.value.activeFilter)
                } else {
                    null to null
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        transactions = transactionList,
                        totalIncome = totalIncome,
                        totalExpense = totalExpense,
                        netBalance = net,
                        emptyStateTitle = emptyTitle,
                        emptyStateMessage = emptyMessage,
                        error = null
                    )
                }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
            .launchIn(viewModelScope)
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

                    _navigationEvent.emit(TransactionRoutes.FilterTransactions)
                }
            }

            is TransactionListEvent.RemoveFilterChip -> {
                removeFilter(event.chip)
            }

            is TransactionListEvent.TransactionClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(TransactionRoutes.TransactionDetail(event.transactionId))
                }
            }
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

        // --- Logika Tanggal yang Jauh Lebih Sederhana ---
        val dateLabel = when (filter.dateOption) {
            DateRangeOption.CUSTOM -> {
                val startDate = filter.startDate
                val endDate = filter.endDate
                // Jika custom, format tanggalnya
                if (startDate != null && endDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("d MMM")
                    val label = "${startDate.format(formatter)} - ${endDate.format(formatter)}"
                } else {
                    filter.dateOption.displayName
                }
            }
            // Jangan tampilkan chip untuk "All Time" karena itu defaultnya
            DateRangeOption.ALL_TIME -> null
            // Untuk semua opsi lain, cukup gunakan nama dari enum
            else -> filter.dateOption.displayName
        }

        if (dateLabel != null) {
            chips.add(ActiveFilterChip("DATE_RANGE", dateLabel.toString(), FilterChipType.DATE_RANGE))
        }


        // Logika untuk akun dan kategori tidak perlu diubah, sudah bagus.
        filter.accountIds?.let { ids ->
            if (ids.isNotEmpty()) {
                val accounts = getAccountsByIdsUseCase(ids)
                accounts.forEach { account ->
                    chips.add(ActiveFilterChip(account.id, account.name, FilterChipType.ACCOUNT))
                }
            }
        }

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

            FilterChipType.DATE_RANGE -> currentFilter.copy(
                startDate = null,
                endDate = null
            )
        }
        _uiState.update { it.copy(activeFilter = newFilter) }
    }

    private fun determineEmptyState(filter: TransactionFilter): Pair<String, String> {
        return when {
            filter.searchQuery.isNotBlank() ->
                "No Results Found" to "Try using different keywords for your search."

            filter.hasActiveFilters() ->
                "No Transactions Match" to "There are no transactions that match your current filters."

            else ->
                "No Transactions Yet" to "Tap the '+' button to add your first transaction!"
        }
    }

    fun TransactionFilter.hasActiveFilters(): Boolean {
        return !this.accountIds.isNullOrEmpty() || !this.categoryIds.isNullOrEmpty() || this.type != null
    }
}

