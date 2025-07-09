package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesUseCase
import com.rifqi.trackfunds.feature.transaction.ui.event.FilterEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.DateRangeOption
import com.rifqi.trackfunds.feature.transaction.ui.state.FilterTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FilterTransactionViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val resultManager: NavigationResultManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterTransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        val initialFilter = resultManager.argument.value as? TransactionFilter


        if (initialFilter != null) {
            // 2. Terjemahkan filter tanggal menjadi pilihan yang sesuai
            val (initialDateOption, customStart, customEnd) = determineDateOptionFrom(initialFilter)

            // 3. Jika ada, gunakan untuk mengisi state awal
            _uiState.update {
                it.copy(
                    selectedCategoryIds = initialFilter.categoryIds?.toSet() ?: emptySet(),
                    selectedAccountIds = initialFilter.accountIds?.toSet() ?: emptySet(),
                    selectedDateOption = initialDateOption,
                    customStartDate = customStart,
                    customEndDate = customEnd
                )
            }
            // 4. Bersihkan titipan agar tidak dipakai lagi jika pengguna kembali
            resultManager.setArgument(null)
        }

        loadInitialData()
    }

    private fun determineDateOptionFrom(filter: TransactionFilter): Triple<DateRangeOption, LocalDate?, LocalDate?> {
        val today = LocalDate.now()
        val startDate = filter.startDate
        val endDate = filter.endDate

        return when {
            startDate == null && endDate == null -> Triple(DateRangeOption.ALL_TIME, null, null)
            startDate == today && endDate == today -> Triple(DateRangeOption.TODAY, null, null)
            startDate == today.minusDays(6) && endDate == today -> Triple(
                DateRangeOption.LAST_7_DAYS,
                null,
                null
            )

            startDate == today.minusMonths(1).plusDays(1) && endDate == today -> Triple(
                DateRangeOption.LAST_30_DAYS,
                null,
                null
            )

            startDate == today.minusMonths(3).plusDays(1) && endDate == today -> Triple(
                DateRangeOption.LAST_90_DAYS,
                null,
                null
            )
            // Jika tidak cocok dengan pola di atas, anggap sebagai kustom
            else -> Triple(DateRangeOption.CUSTOM, startDate, endDate)
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            combine(
                getCategoriesUseCase(),
                getAccountsUseCase()
            ) { categories, accounts ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        allCategories = categories,
                        allAccounts = accounts
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false /* error = e.message */) }
            }.collect()
        }
    }

    fun onEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.AccountToggled -> {
                val newSet = _uiState.value.selectedAccountIds.toMutableSet()
                if (newSet.contains(event.accountId)) newSet.remove(event.accountId)
                else newSet.add(event.accountId)
                _uiState.update { it.copy(selectedAccountIds = newSet) }
            }

            is FilterEvent.CategoryToggled -> {
                val newSet = _uiState.value.selectedCategoryIds.toMutableSet()
                if (newSet.contains(event.categoryId)) newSet.remove(event.categoryId)
                else newSet.add(event.categoryId)
                _uiState.update { it.copy(selectedCategoryIds = newSet) }
            }

            is FilterEvent.DateRangeSelected -> {
                _uiState.update {
                    it.copy(
                        selectedDateRange = Pair(
                            event.startDate,
                            event.endDate
                        )
                    )
                }
            }

            is FilterEvent.DateOptionSelected -> {
                _uiState.update { it.copy(selectedDateOption = event.option) }
            }

            FilterEvent.ManualDateSelectionClicked -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            FilterEvent.DatePickerDismissed -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is FilterEvent.CustomDateSelected -> {
                _uiState.update {
                    it.copy(
                        selectedDateOption = DateRangeOption.CUSTOM,
                        customStartDate = event.startDate,
                        customEndDate = event.endDate,
                        showDatePicker = false
                    )
                }
            }

            FilterEvent.ApplyFilterClicked -> {
                applyFilterAndNavigateBack()
            }
        }
    }

    private fun applyFilterAndNavigateBack() {
        val state = _uiState.value
        val (startDate, endDate) = calculateDateRange()
        val finalFilter = TransactionFilter(
            categoryIds = state.selectedCategoryIds.toList().ifEmpty { null },
            accountIds = state.selectedAccountIds.toList().ifEmpty { null },
            startDate = startDate,
            endDate = endDate,
        )

        resultManager.setResult(finalFilter)

        viewModelScope.launch {
            _navigationEvent.emit(Unit)
        }
    }

    private fun calculateDateRange(): Pair<LocalDate?, LocalDate?> {
        val today = LocalDate.now()
        val state = _uiState.value
        return when (state.selectedDateOption) {
            DateRangeOption.ALL_TIME -> Pair(null, null)
            DateRangeOption.TODAY -> Pair(today, today)
            DateRangeOption.LAST_7_DAYS -> Pair(today.minusDays(6), today)
            DateRangeOption.LAST_30_DAYS -> Pair(today.minusMonths(1).plusDays(1), today)
            DateRangeOption.LAST_90_DAYS -> Pair(today.minusMonths(3).plusDays(1), today)
            DateRangeOption.CUSTOM -> Pair(state.customStartDate, state.customEndDate)
        }
    }
}