package com.rifqi.trackfunds.feature.transaction.ui.addEditTransaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.feature.transaction.ui.model.LineItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val resultManager: NavigationResultManager,
    private val getCategoryByStandardKeyUseCase: GetCategoryByStandardKeyUseCase,
    private val getActiveSavingsGoalsUseCase: GetActiveSavingsGoalsUseCase,
    private val addFundsToSavingsGoalUseCase: AddFundsToSavingsGoalUseCase,
    private val snackbarManager: SnackbarManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: TransactionRoutes.AddEditTransaction = savedStateHandle.toRoute()
    private val editingTransactionId: String? = args.transactionId
    val isEditMode: Boolean = editingTransactionId != null

    private var originalTransaction: Transaction? = null

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AddEditTransactionSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesForSelection: StateFlow<List<Category>> =
        _uiState.map { it.selectedTransactionType to it.categorySearchQuery }
            .distinctUntilChanged()
            .flatMapLatest { (type, query) ->
                getFilteredCategoriesUseCase(CategoryFilter(type = type))
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
        loadInitialData()
        observeNavigationResults()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val allAccounts = getAccountsUseCase().first()
            val allSavingsGoals = getActiveSavingsGoalsUseCase().first()

            _uiState.update {
                it.copy(
                    allAccounts = allAccounts,
                    allSavingsGoals = allSavingsGoals
                )
            }

            if (isEditMode) {
                loadTransactionForEdit(editingTransactionId!!)
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadTransactionForEdit(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTransactionByIdUseCase(id).first()?.let { transaction ->
                originalTransaction = transaction
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        amount = transaction.amount.toPlainString(),
                        selectedTransactionType = transaction.type,
                        selectedDate = transaction.date.toLocalDate(),
                        selectedAccount = transaction.account,
                        selectedCategory = transaction.category,
                        description = transaction.description
                    )
                }
            } ?: _uiState.update { it.copy(isLoading = false, error = "Transaction not found.") }
        }
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.AmountChanged -> if (event.amount.all { it.isDigit() }) {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is AddEditTransactionEvent.TypeChanged -> {
                _uiState.update {
                    it.copy(
                        selectedTransactionType = event.type,
                        selectedCategory = null,
                        selectedSavingsGoal = null
                    )
                }
            }

            is AddEditTransactionEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is AddEditTransactionEvent.DateSelected -> _uiState.update {
                it.copy(
                    selectedDate = event.date,
                    showDatePicker = false
                )
            }

            AddEditTransactionEvent.DateSelectorClicked -> _uiState.update { it.copy(showDatePicker = true) }

            AddEditTransactionEvent.DismissDatePicker -> {
                _uiState.update { it.copy(showDatePicker = false) }
            }

            AddEditTransactionEvent.CategorySelectorClicked -> _uiState.update {
                it.copy(
                    activeSheet = AddEditSheetType.CATEGORY,
                )
            }

            AddEditTransactionEvent.AccountSelectorClicked -> _uiState.update {
                it.copy(
                    activeSheet = AddEditSheetType.ACCOUNT,
                )
            }

            AddEditTransactionEvent.SavingsGoalSelectorClicked -> {
                _uiState.update {
                    it.copy(
                        activeSheet = AddEditSheetType.SAVINGS_GOAL
                    )
                }
            }

            AddEditTransactionEvent.DismissSheet -> _uiState.update {
                it.copy(
                    activeSheet = null,
                )
            }

            is AddEditTransactionEvent.CategorySearchChanged -> {
                _uiState.update { it.copy(categorySearchQuery = event.query) }
            }

            is AddEditTransactionEvent.CategorySelected -> {
                _uiState.update {
                    it.copy(
                        selectedCategory = event.category,
                        activeSheet = null
                    )
                }
            }

            is AddEditTransactionEvent.AccountSelected -> {
                _uiState.update {
                    it.copy(
                        selectedAccount = event.account,
                        activeSheet = null
                    )
                }
            }

            is AddEditTransactionEvent.SavingsGoalSelected -> {
                _uiState.update {
                    it.copy(
                        selectedSavingsGoal = event.goal,
                        activeSheet = null
                    )
                }
            }

            AddEditTransactionEvent.SaveClicked -> saveTransaction()
            is AddEditTransactionEvent.ToggleDetailsSection -> {
                _uiState.update { it.copy(isDetailsExpanded = !it.isDetailsExpanded) }
            }

            is AddEditTransactionEvent.AddNewLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.lineItems + LineItemUiModel() // Tambah item baru
                    currentState.copy(lineItems = newList)
                }
            }

            is AddEditTransactionEvent.DeleteLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.lineItems.toMutableList().apply {
                        removeAt(event.index) // Hapus item pada indeks tertentu
                    }
                    currentState.copy(lineItems = newList)
                }
            }

            is AddEditTransactionEvent.OnLineItemChanged -> {
                _uiState.update { currentState ->
                    val newList = currentState.lineItems.toMutableList().apply {
                        this[event.index] = event.item
                    }
                    currentState.copy(lineItems = newList)
                }
            }

            is AddEditTransactionEvent.OnAddReceiptClicked -> {
                viewModelScope.launch {
                    _sideEffect.emit(AddEditTransactionSideEffect.LaunchGallery)
                }
            }

            is AddEditTransactionEvent.OnReceiptImageSelected -> {
                // UI telah memilih gambar, sekarang update URI-nya di state
                _uiState.update { it.copy(receiptImageUri = event.uri) }
            }

            is AddEditTransactionEvent.OnDeleteLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.lineItems.toMutableList().apply {
                        removeAt(event.index)
                    }
                    currentState.copy(lineItems = newList)
                }
            }
        }
    }

    private fun observeNavigationResults() {
        resultManager.result.onEach { resultData ->
            when (resultData) {
                is ScanResult -> handleScanResult(resultData)
            }
            if (resultData != null) resultManager.setResult(null)
        }.launchIn(viewModelScope)
    }

    private fun handleScanResult(scanResult: ScanResult) {
        viewModelScope.launch {
            var suggestedCategory: Category? = null


            scanResult.categoryStandardKey?.let { key ->
                suggestedCategory = getCategoryByStandardKeyUseCase(key)
            }

            _uiState.update {
                it.copy(
                    amount = scanResult.totalAmount.toPlainString() ?: it.amount,
                    description = scanResult.merchantName ?: it.description,
                    selectedDate = scanResult.transactionDateTime.toLocalDate() ?: it.selectedDate,
                    selectedCategory = suggestedCategory ?: it.selectedCategory
                )
            }
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val currentState = _uiState.value

            _uiState.update { it.copy(isSaving = true) }

            val transactionToSave = createTransactionFromState(currentState)

            try {
                if (isEditMode) {
                    updateTransactionUseCase(
                        transaction = transactionToSave,
                        oldAmount = originalTransaction!!.amount,
                        oldAccountId = originalTransaction!!.account.id
                    )
                    snackbarManager.showMessage("Transaction successfully updated")
                } else {
                    // Cek apakah ini setoran tabungan
                    if (transactionToSave.savingsGoal != null) {
                        addTransactionUseCase(transactionToSave)
                        addFundsToSavingsGoalUseCase(
                            goalId = transactionToSave.savingsGoal!!.id,
                            amount = transactionToSave.amount
                        )
                        snackbarManager.showMessage("Setoran berhasil ditambahkan")
                    } else {
                        addTransactionUseCase(transactionToSave)
                        snackbarManager.showMessage("Transaction successfully saved")
                    }
                }
                TODO("Implement navigation to home screen after saving transaction")
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    // Fungsi helper untuk membuat objek TransactionItem
    private fun createTransactionFromState(state: AddEditTransactionUiState): Transaction {
        val isSavings = state.selectedTransactionType == TransactionType.SAVINGS
        return Transaction(
            id = editingTransactionId ?: UUID.randomUUID().toString(),
            description = state.description,
            amount = state.amount.toBigDecimal(),
            type = if (isSavings) TransactionType.SAVINGS else state.selectedTransactionType,
            date = state.selectedDate.atTime(LocalTime.now()),
            category = if (isSavings) null else state.selectedCategory,
            account = state.selectedAccount!!,
            savingsGoal = if (isSavings) state.selectedSavingsGoal else null
        )
    }
}