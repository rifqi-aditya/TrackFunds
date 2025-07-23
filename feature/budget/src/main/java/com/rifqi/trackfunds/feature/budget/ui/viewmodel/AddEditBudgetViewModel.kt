package com.rifqi.trackfunds.feature.budget.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.navigation.api.BudgetRoutes
import com.rifqi.trackfunds.feature.budget.ui.event.AddEditBudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.state.AddEditBudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val addBudgetUseCase: AddBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val resultManager: NavigationResultManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: BudgetRoutes.AddEditBudget = savedStateHandle.toRoute()
    private val editingBudgetId: String? = args.budgetId

    val isEditMode: Boolean = editingBudgetId != null

    private val _uiState = MutableStateFlow(
        AddEditBudgetUiState(period = YearMonth.parse(args.period))
    )
    val uiState: StateFlow<AddEditBudgetUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        if (isEditMode) {
            loadBudgetForEdit(editingBudgetId!!)
        }
        observeNavigationResult()
    }

    private fun loadBudgetForEdit(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Panggil UseCase dan tangani hasilnya
            getBudgetByIdUseCase(id)
                .onSuccess { budget ->
                    // Blok ini berjalan jika budget berhasil ditemukan
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = CategoryItem(
                                id = budget.categoryId,
                                name = budget.categoryName,
                                iconIdentifier = budget.categoryIconIdentifier.toString(),
                                type = TransactionType.EXPENSE
                            ),
                            amount = budget.budgetAmount.toPlainString(),
                            initialSpentAmount = budget.spentAmount // Simpan spent amount awal
                        )
                    }
                }
                .onFailure { error ->
                    // Blok ini berjalan jika terjadi error (misal: budget tidak ditemukan)
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun observeNavigationResult() {
        resultManager.result.onEach { result ->
            if (result is CategoryItem) {
                onEvent(AddEditBudgetEvent.CategorySelected(result))
                resultManager.setResult(null)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: AddEditBudgetEvent) {
        when (event) {
            is AddEditBudgetEvent.AmountChanged -> if (event.amount.all { it.isDigit() }) {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is AddEditBudgetEvent.CategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
            }

            AddEditBudgetEvent.SaveBudgetClicked -> saveBudget()
            AddEditBudgetEvent.CategorySelectorClicked -> {
                viewModelScope.launch {
                    val periodString =
                        _uiState.value.period.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    _navigationEvent.emit(periodString)
                }
            }

            AddEditBudgetEvent.DeleteClicked -> _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            AddEditBudgetEvent.ConfirmDeleteClicked -> deleteBudget()
            AddEditBudgetEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }
        }
    }

    private fun saveBudget() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedCategory != null && state.amount.isNotBlank()) {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val budgetToSave = BudgetItem(
                        budgetId = editingBudgetId ?: UUID.randomUUID()
                            .toString(),
                        categoryId = state.selectedCategory.id,
                        categoryName = state.selectedCategory.name,
                        categoryIconIdentifier = state.selectedCategory.iconIdentifier,
                        budgetAmount = BigDecimal(state.amount),
                        spentAmount = if (isEditMode) state.initialSpentAmount else BigDecimal.ZERO, // FIX: Gunakan spent amount yang tersimpan
                        period = state.period.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    )

                    if (isEditMode) {
                        updateBudgetUseCase(budgetToSave)
                    } else {
                        addBudgetUseCase(budgetToSave)
                    }
                    _uiState.update { it.copy(isBudgetSaved = true) }
                } catch (e: Exception) {
                    Log.e("AddEditBudgetViewModel", "Error saving budget: ${e.message}")
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }

    private fun deleteBudget() {
        if (isEditMode) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
                try {
                    deleteBudgetUseCase(editingBudgetId!!)
                    _uiState.update { it.copy(isBudgetSaved = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }
}