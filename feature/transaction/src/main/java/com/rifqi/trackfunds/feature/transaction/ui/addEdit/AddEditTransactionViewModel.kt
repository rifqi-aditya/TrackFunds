package com.rifqi.trackfunds.feature.transaction.ui.addEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.category.model.AddTransactionParams
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.model.UpdateTransactionParams
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.scan.model.ScanResult
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionItem
import com.rifqi.trackfunds.core.domain.transaction.usecase.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.validators.ValidateTransactionUseCase
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.core.ui.utils.formatNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val transactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val transactionAddUseCase: AddTransactionUseCase,
    private val transactionUpdateUseCase: UpdateTransactionUseCase,
    private val accountsUseCase: GetAccountsUseCase,
    private val filteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val resultManager: NavigationResultManager,
    private val categoryByStandardKeyUseCase: GetCategoryByStandardKeyUseCase,
    private val activeSavingsGoalsUseCase: GetActiveSavingsGoalsUseCase,
    private val snackbarManager: SnackbarManager,
    private val validateTransactionUseCase: ValidateTransactionUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: TransactionRoutes.AddEditTransaction = savedStateHandle.toRoute()
    private val editingTransactionId: String? = args.transactionId
    val isEditMode: Boolean = editingTransactionId != null

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<AddEditTransactionSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesForSelection: StateFlow<List<Category>> =
        _uiState.map { it.selectedTransactionType to it.categorySearchQuery }
            .distinctUntilChanged()
            .flatMapLatest { (type, query) ->
                filteredCategoriesUseCase(CategoryFilter(type = type))
                    .map { categories ->
                        if (query.isBlank()) categories
                        else categories.filter { it.name.contains(query, ignoreCase = true) }
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
            val allAccounts = accountsUseCase().first()
            val allSavingsGoals = activeSavingsGoalsUseCase().first()

            _uiState.update {
                it.copy(
                    allAccounts = allAccounts,
                    allSavingsGoals = allSavingsGoals,
                    selectedAccount = if (!isEditMode) allAccounts.firstOrNull() else it.selectedAccount
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
            transactionDetailsUseCase(id).first()?.let { transaction ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        amount = transaction.amount.toPlainString(),
                        selectedTransactionType = transaction.type,
                        selectedDate = transaction.date.toLocalDate(),
                        selectedAccount = transaction.account,
                        selectedCategory = transaction.category,
                        description = transaction.description,
                        items = transaction.items.map { domainItem -> domainItem.toUiModel() }
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

            is AddEditTransactionEvent.AddNewLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.items + TransactionItemInput()
                    currentState.copy(items = newList)
                }
            }

            is AddEditTransactionEvent.DeleteLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.items.toMutableList().apply {
                        removeAt(event.index)
                    }
                    currentState.copy(items = newList)
                }
            }

            is AddEditTransactionEvent.OnLineItemChanged -> {
                _uiState.update { currentState ->
                    val newList = currentState.items.toMutableList()
                    val itemToUpdate = event.item

                    val quantityError =
                        if (itemToUpdate.quantity.toLongOrNull() == null && itemToUpdate.quantity.isNotEmpty()) "Invalid" else null
                    val priceError =
                        if (itemToUpdate.price.toBigDecimalOrNull() == null && itemToUpdate.price.isNotEmpty()) "Invalid" else null
                    val nameError = if (itemToUpdate.name.isBlank()) "Required" else null

                    newList[event.index] = itemToUpdate.copy(
                        quantityError = quantityError,
                        priceError = priceError,
                        nameError = nameError
                    )
                    currentState.copy(items = newList)
                }
            }

            is AddEditTransactionEvent.OnAddReceiptClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(AddEditTransactionSideEffect.LaunchGallery)
                }
            }

            is AddEditTransactionEvent.OnReceiptImageSelected -> {
                _uiState.update { it.copy(receiptImageUri = event.uri) }
            }

            is AddEditTransactionEvent.OnDeleteLineItem -> {
                _uiState.update { currentState ->
                    val newList = currentState.items.toMutableList().apply {
                        removeAt(event.index)
                    }
                    currentState.copy(items = newList)
                }
            }

            AddEditTransactionEvent.ToggleLineItemsSection -> {
                _uiState.update { currentState ->
                    currentState.copy(isItemsExpanded = !currentState.isItemsExpanded)
                }
            }

            AddEditTransactionEvent.ToggleReceiptSection -> {
                _uiState.update { currentState ->
                    currentState.copy(isReceiptExpanded = !currentState.isReceiptExpanded)
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
                suggestedCategory = categoryByStandardKeyUseCase(key)
            }

            _uiState.update {
                it.copy(
                    amount = formatNumber(scanResult.totalAmount),
                    description = scanResult.merchantName ?: it.description,
                    selectedDate = scanResult.transactionDateTime.toLocalDate() ?: it.selectedDate,
                    selectedCategory = suggestedCategory ?: it.selectedCategory,
                    items = scanResult.transactionItem.map { item ->
                        item.toUiModel()
                    },
                    receiptImageUri = scanResult.receiptImageUri ?: it.receiptImageUri,
                    isItemsExpanded = true,
                )
            }
        }
    }

    private fun saveTransaction() {
        val currentState = uiState.value

        val validationResult = validateTransactionUseCase(
            amount = currentState.amount,
            account = currentState.selectedAccount,
            category = currentState.selectedCategory,
            date = currentState.selectedDate
        )

        if (!validationResult.isSuccess) {
            _uiState.update {
                it.copy(
                    amountError = validationResult.amountError,
                    accountError = validationResult.accountError,
                    categoryError = validationResult.categoryError,
                    dateError = validationResult.dateError
                )
            }
            return // Hentikan proses
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val result: Result<Unit> = if (isEditMode) {
                val params = UpdateTransactionParams(
                    id = editingTransactionId!!,
                    description = currentState.description,
                    amount = currentState.amountAsBigDecimal,
                    type = currentState.selectedTransactionType,
                    date = currentState.selectedDate,
                    account = currentState.selectedAccount!!,
                    category = currentState.selectedCategory,
                    savingsGoal = currentState.selectedSavingsGoal,
                    items = currentState.items.mapNotNull { it.toDomainModel() }
                )
                transactionUpdateUseCase(params)
            } else {
                val params = AddTransactionParams(
                    description = currentState.description,
                    amount = currentState.amountAsBigDecimal,
                    type = currentState.selectedTransactionType,
                    date = currentState.selectedDate,
                    account = currentState.selectedAccount!!,
                    category = currentState.selectedCategory,
                    savingsGoal = currentState.selectedSavingsGoal,
                    items = currentState.items.mapNotNull { it.toDomainModel() }
                )
                transactionAddUseCase(params)
            }

            result.onSuccess {
                val message = if (isEditMode) "Transaction updated" else "Transaction saved"
                snackbarManager.showMessage(message)
                _sideEffect.send(AddEditTransactionSideEffect.NavigateBack)
            }.onFailure { exception ->
                _uiState.update { it.copy(isSaving = false, error = exception.message) }
            }
        }
    }

    private fun TransactionItemInput.toDomainModel(): TransactionItem? =
        TransactionItem(
            name = name,
            price = priceAsBigDecimal,
            quantity = quantity.toIntOrNull() ?: 1
        )

    private fun TransactionItem.toUiModel(): TransactionItemInput =
        TransactionItemInput(
            id = UUID.randomUUID().toString(),
            name = this.name,
            quantity = this.quantity.toString(),
            price = formatNumber(this.price)
        )
}
