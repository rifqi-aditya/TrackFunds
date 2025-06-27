package com.rifqi.trackfunds.feature.budget.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditBudget
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.feature.budget.ui.event.BudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.model.BudgetTab
import com.rifqi.trackfunds.feature.budget.ui.state.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    val filteredBudgets: StateFlow<List<BudgetItem>> =
        combine(
            // Ambil hanya list budget mentah dari state
            _uiState.map { it.budgets }.distinctUntilChanged(),
            // Ambil hanya tab yang dipilih
            _uiState.map { it.selectedTab }.distinctUntilChanged(),
            // Ambil hanya filter kategori yang dipilih
            _uiState.map { it.selectedCategoryFilterId }.distinctUntilChanged()
        ) { budgets, selectedTab, selectedCategoryFilter ->
            // Langkah 1: Tentukan daftar mana yang akan ditampilkan berdasarkan tab
            val listForCurrentTab = when (selectedTab) {
                BudgetTab.YOUR_BUDGETS -> budgets // Jika tab "Your Budgets", gunakan daftar budget utama
                BudgetTab.RECURRING_BUDGETS -> emptyList() // Jika tab "Recurring", KOSONGKAN untuk sekarang
            }

            // Langkah 2: Terapkan filter kategori jika ada
            if (selectedCategoryFilter == null) {
                listForCurrentTab // Jika tidak ada filter, kembalikan daftar dari tab
            } else {
                // Jika ada filter, filter daftar dari tab tersebut
                listForCurrentTab.filter { it.categoryId == selectedCategoryFilter }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadBudgets()
    }

    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.ChangePeriod -> {
                _uiState.update { it.copy(currentPeriod = event.newPeriod) }
                loadBudgets()
            }
            // FIX 2: Tangani event klik tombol
            BudgetEvent.AddBudgetClicked -> {
                viewModelScope.launch {
                    val periodString =
                        _uiState.value.currentPeriod.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    // Kirim sinyal untuk navigasi ke mode Add (budgetId = null)
                    _navigationEvent.emit(AddEditBudget(period = periodString))
                }
            }

            is BudgetEvent.EditBudgetClicked -> {
                viewModelScope.launch {
                    val periodString =
                        _uiState.value.currentPeriod.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    // Kirim sinyal untuk navigasi ke mode Edit dengan budgetId
                    _navigationEvent.emit(
                        AddEditBudget(
                            period = periodString,
                            budgetId = event.budgetId
                        )
                    )
                }
            }

            is BudgetEvent.CategoryFilterClicked -> {
                val currentFilter = _uiState.value.selectedCategoryFilterId
                _uiState.update {
                    it.copy(selectedCategoryFilterId = if (currentFilter == event.categoryId) null else event.categoryId)
                }
            }

            BudgetEvent.ClearCategoryFilter -> _uiState.update {
                it.copy(
                    selectedCategoryFilterId = null
                )
            }

            is BudgetEvent.TabSelected -> _uiState.update { it.copy(selectedTab = event.tab) }
            BudgetEvent.ChangePeriodClicked -> {
                _uiState.update { it.copy(showMonthPickerDialog = true) }
            }

            is BudgetEvent.PeriodSelected -> {
                _uiState.update {
                    it.copy(
                        currentPeriod = event.newPeriod,
                        showMonthPickerDialog = false
                    )
                }
                loadBudgets()
            }

            BudgetEvent.DialogDismissed -> _uiState.update { it.copy(showMonthPickerDialog = false) }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            val period = _uiState.value.currentPeriod

            getBudgetsUseCase(period)
                .onStart {
                    // Tampilkan loading dan hapus error sebelumnya
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    // Jika ada error, hentikan loading dan tampilkan pesan
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { budgetItems ->
                    // Hitung total dari daftar budget yang diterima
                    val totalBudgeted = budgetItems.mapNotNull { it.budgetAmount }.sumOf { it }
                    val totalSpent = budgetItems.sumOf { it.spentAmount }
                    val categories = budgetItems.map {
                        CategoryItem(
                            it.categoryId,
                            it.categoryName,
                            it.categoryIconIdentifier.toString(),
                            TransactionType.EXPENSE
                        )
                    }.distinctBy { it.id }


                    // Update state dengan data yang berhasil dimuat
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            budgets = budgetItems,
                            totalBudgeted = totalBudgeted,
                            totalSpent = totalSpent,
                            categoriesWithBudget = categories,
                        )
                    }
                }
        }
    }
}