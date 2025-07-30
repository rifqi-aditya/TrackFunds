package com.rifqi.trackfunds.feature.budget.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.model.Budget
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.CheckExistingBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.navigation.api.BudgetRoutes
import com.rifqi.trackfunds.feature.budget.ui.event.AddEditBudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.sideeffect.AddEditBudgetSideEffect
import com.rifqi.trackfunds.feature.budget.ui.state.AddEditBudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.YearMonth
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val addBudgetUseCase: AddBudgetUseCase,
    private val checkExistingBudgetUseCase: CheckExistingBudgetUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: BudgetRoutes.AddEditBudget = savedStateHandle.toRoute()
    private val editingBudgetId: String? = args.budgetId
    val isEditMode: Boolean = editingBudgetId != null

    private val _uiState =
        MutableStateFlow(AddEditBudgetUiState(period = YearMonth.parse(args.period)))
    val uiState: StateFlow<AddEditBudgetUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AddEditBudgetSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesForSelection: StateFlow<List<Category>> =
        _uiState.map { it.categorySearchQuery }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                getFilteredCategoriesUseCase(CategoryFilter(type = TransactionType.EXPENSE))
                    .map { categories ->
                        if (query.isBlank()) {
                            categories
                        } else {
                            categories.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        if (isEditMode) {
            loadBudgetForEdit(editingBudgetId!!)
        }
    }

    // --- Event Dispatcher ---
    fun onEvent(event: AddEditBudgetEvent) {
        when (event) {
            is AddEditBudgetEvent.AmountChanged -> handleAmountChanged(event.amount)
            is AddEditBudgetEvent.CategorySelected -> handleCategorySelected(event.category)
            is AddEditBudgetEvent.CategorySearchChanged -> _uiState.update {
                it.copy(
                    categorySearchQuery = event.query
                )
            }

            is AddEditBudgetEvent.PeriodSelected -> _uiState.update {
                it.copy(
                    period = event.period,
                    showPeriodPicker = false
                )
            }

            AddEditBudgetEvent.SaveBudgetClicked -> handleSaveBudget()
            AddEditBudgetEvent.ConfirmDeleteClicked -> handleDeleteBudget()
            AddEditBudgetEvent.CategorySelectorClicked -> _uiState.update {
                it.copy(
                    showCategorySheet = true
                )
            }

            AddEditBudgetEvent.DismissCategorySheet -> _uiState.update { it.copy(showCategorySheet = false) }
            AddEditBudgetEvent.DeleteClicked -> _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            AddEditBudgetEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }

            AddEditBudgetEvent.PeriodSelectorClicked -> _uiState.update { it.copy(showPeriodPicker = true) }
            AddEditBudgetEvent.DismissPeriodPicker -> _uiState.update { it.copy(showPeriodPicker = false) }
            AddEditBudgetEvent.ShowCategorySheet -> _uiState.update {
                it.copy(showCategorySheet = true)
            }
        }
    }

    // --- Private Event Handlers ---

    private fun handleAmountChanged(amount: String) {
        if (amount.all { it.isDigit() }) {
            _uiState.update { it.copy(amount = amount, error = null) }
        }
    }

    private fun handleCategorySelected(category: Category) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                showCategorySheet = false,
                error = null
            )
        }
        checkIfBudgetExists(category.id)
    }

    private fun handleSaveBudget() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value

            val budgetToSave = Budget(
                budgetId = editingBudgetId ?: UUID.randomUUID().toString(),
                categoryId = state.selectedCategory?.id ?: "",
                categoryName = state.selectedCategory?.name ?: "",
                categoryIconIdentifier = state.selectedCategory?.iconIdentifier ?: "",
                budgetAmount = state.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                spentAmount = if (isEditMode) state.initialSpentAmount else BigDecimal.ZERO,
                period = state.period
            )

            val result = if (isEditMode) {
                updateBudgetUseCase(budgetToSave)
            } else {
                addBudgetUseCase(budgetToSave)
            }

            result
                .onSuccess { _sideEffect.emit(AddEditBudgetSideEffect.NavigateBack) }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun handleDeleteBudget() {
        if (isEditMode) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
                deleteBudgetUseCase(editingBudgetId!!)
                    .onSuccess { _sideEffect.emit(AddEditBudgetSideEffect.NavigateBack) }
                    .onFailure { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message
                            )
                        }
                    }
            }
        }
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
                            selectedCategory = Category(
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

    private fun checkIfBudgetExists(categoryId: String) {
        viewModelScope.launch {
            val existingBudgetId = checkExistingBudgetUseCase(categoryId, _uiState.value.period)
            if (existingBudgetId != null) {
                _sideEffect.emit(
                    AddEditBudgetSideEffect.NavigateToEditMode(
                        budgetId = existingBudgetId,
                        period = _uiState.value.period
                    )
                )
            }
        }
    }
}