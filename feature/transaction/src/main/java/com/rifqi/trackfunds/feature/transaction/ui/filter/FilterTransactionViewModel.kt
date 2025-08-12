package com.rifqi.trackfunds.feature.transaction.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class FilterTransactionViewModel @Inject constructor(
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val resultManager: NavigationResultManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterTransactionUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        // Ambil filter yang ada saat ini dari layar sebelumnya
        val initialFilter = resultManager.argument.value as? TransactionFilter
        if (initialFilter != null) {
            // Langsung terapkan state dari filter yang ada
            _uiState.update {
                it.copy(
                    selectedCategoryIds = initialFilter.categoryIds?.toSet() ?: emptySet(),
                    selectedAccountIds = initialFilter.accountIds?.toSet() ?: emptySet(),
                    // Anda perlu menambahkan `dateOption` ke TransactionFilter Anda
                    // selectedDateOption = initialFilter.dateOption
                )
            }
            resultManager.setArgument(null)
        }
        loadChoiceData()
    }

    private fun loadChoiceData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(
                getFilteredCategoriesUseCase(CategoryFilter()),
                getAccountsUseCase()
            ) { categories, accounts ->
                _uiState.update {
                    it.copy(isLoading = false, allCategories = categories, allAccounts = accounts)
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect()
        }
    }

    fun onEvent(event: FilterTransactionEvent) {
        when (event) {
            is FilterTransactionEvent.AccountToggled -> {
                val newSet = _uiState.value.selectedAccountIds.toMutableSet()
                if (newSet.contains(event.accountId)) newSet.remove(event.accountId)
                else newSet.add(event.accountId)
                _uiState.update { it.copy(selectedAccountIds = newSet) }
            }

            is FilterTransactionEvent.CategoryToggled -> {
                val newSet = _uiState.value.selectedCategoryIds.toMutableSet()
                if (newSet.contains(event.categoryId)) newSet.remove(event.categoryId)
                else newSet.add(event.categoryId)
                _uiState.update { it.copy(selectedCategoryIds = newSet) }
            }

            is FilterTransactionEvent.DateRangeSelected -> {
                _uiState.update {
                    it.copy(
                        selectedDateRange = Pair(
                            event.startDate,
                            event.endDate
                        )
                    )
                }
            }

            is FilterTransactionEvent.DateOptionSelected -> {
                _uiState.update { it.copy(selectedDateOption = event.option) }
            }

            FilterTransactionEvent.ManualDateSelectionClicked -> {
                _uiState.update { it.copy(showDatePicker = true) }
            }

            FilterTransactionEvent.DatePickerDismissed -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            is FilterTransactionEvent.CustomDateSelected -> {
                _uiState.update {
                    it.copy(
                        selectedDateOption = DateRangeOption.CUSTOM,
                        customStartDate = event.startDate,
                        customEndDate = event.endDate,
                        showDatePicker = false
                    )
                }
            }

            FilterTransactionEvent.ApplyFilterTransactionClicked -> {
                applyFilterAndNavigateBack()
            }

            FilterTransactionEvent.ClearFiltersClicked -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryIds = emptySet(),
                        selectedAccountIds = emptySet(),
                        selectedDateOption = DateRangeOption.THIS_MONTH,
                        customStartDate = null,
                        customEndDate = null
                    )
                }
            }
        }
    }

    private fun applyFilterAndNavigateBack() {
        val state = _uiState.value
        val (startDate, endDate) = calculateDateRange(state)
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

    private fun calculateDateRange(state: FilterTransactionUiState): Pair<LocalDate?, LocalDate?> {
        val today = LocalDate.now()
        return when (state.selectedDateOption) {
            DateRangeOption.THIS_WEEK -> today.with(DayOfWeek.MONDAY) to today
            DateRangeOption.THIS_MONTH -> today.withDayOfMonth(1) to today.with(TemporalAdjusters.lastDayOfMonth())
            DateRangeOption.LAST_MONTH -> today.minusMonths(1).withDayOfMonth(1).let {
                it to it.with(
                    TemporalAdjusters.lastDayOfMonth()
                )
            }

            DateRangeOption.THIS_YEAR -> today.withDayOfYear(1) to today.with(TemporalAdjusters.lastDayOfYear())
            DateRangeOption.ALL_TIME -> null to null
            DateRangeOption.CUSTOM -> state.customStartDate to state.customEndDate
        }
    }
}