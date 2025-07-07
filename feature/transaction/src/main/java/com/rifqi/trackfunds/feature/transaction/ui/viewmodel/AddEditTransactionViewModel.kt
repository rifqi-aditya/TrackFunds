package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.model.ScanResult
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.navigation.api.ScanOption
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.feature.transaction.ui.event.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.state.AddEditTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val resultManager: NavigationResultManager,
    private val getCategoryByStandardKeyUseCase: GetCategoryByStandardKeyUseCase,
    private val getActiveSavingsGoalsUseCase: GetActiveSavingsGoalsUseCase,
    private val addFundsToSavingsGoalUseCase: AddFundsToSavingsGoalUseCase,
    private val snackbarManager: SnackbarManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args: AddEditTransaction = savedStateHandle.toRoute()
    private val editingTransactionId: String? = args.transactionId

    val isEditMode: Boolean = editingTransactionId != null

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    private val _savingsGoals = MutableStateFlow<List<SavingsGoal>>(emptyList())
    val savingsGoals = _savingsGoals.asStateFlow()

    init {
        if (isEditMode) {
            loadTransactionForEdit(editingTransactionId!!)
        }

        viewModelScope.launch {
            getActiveSavingsGoalsUseCase().collect {
                _savingsGoals.value = it
            }
        }

        observeNavigationResults()
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            // Aksi dari Form Input
            is AddEditTransactionEvent.AmountChanged -> if (event.amount.all { it.isDigit() }) {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is AddEditTransactionEvent.TransactionTypeChanged -> _uiState.update {
                it.copy(
                    selectedTransactionType = event.type,
                    selectedCategory = null,
                    selectedAccount = null,
                    selectedSavingsGoal = null
                )
            }

            is AddEditTransactionEvent.DescriptionChanged -> _uiState.update { it.copy(descriptions = event.descriptions) }
            is AddEditTransactionEvent.DateChanged -> _uiState.update {
                it.copy(
                    selectedDate = event.date,
                    showDatePicker = false
                )
            }

            // Aksi dari Hasil Navigasi (dipanggil dari observeNavigationResult)
            is AddEditTransactionEvent.AccountSelected -> _uiState.update { it.copy(selectedAccount = event.account) }
            is AddEditTransactionEvent.CategorySelected -> _uiState.update {
                it.copy(
                    selectedCategory = event.category
                )
            }

            // Aksi Klik Tombol/UI
            AddEditTransactionEvent.SaveClicked -> saveTransaction()
            AddEditTransactionEvent.DeleteClicked -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = true
                )
            }

            AddEditTransactionEvent.ConfirmDeleteClicked -> deleteTransaction()
            AddEditTransactionEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }

            AddEditTransactionEvent.DateSelectorClicked -> _uiState.update { it.copy(showDatePicker = true) }

            // Aksi untuk memicu navigasi
            AddEditTransactionEvent.AccountSelectorClicked -> viewModelScope.launch {
                _navigationEvent.emit(
                    SelectAccount
                )
            }

            AddEditTransactionEvent.CategorySelectorClicked -> viewModelScope.launch {
                val typeName = _uiState.value.selectedTransactionType.name
                _navigationEvent.emit(SelectCategory(typeName))
            }

            AddEditTransactionEvent.ScanReceiptClicked -> viewModelScope.launch {
                _navigationEvent.emit(
                    ScanOption
                )
            }

            // Aksi untuk mereset state
            AddEditTransactionEvent.ErrorMessageShown -> _uiState.update { it.copy(error = null) }
            AddEditTransactionEvent.NavigationHandled -> viewModelScope.launch {
                _navigationEvent.emit(
                    Home
                )
            }
            // Atau reset
            AddEditTransactionEvent.SavingsGoalSelectorClicked -> {
                _uiState.update { it.copy(showSavingsGoalSheet = true) }
            }

            // Event untuk menutup sheet
            AddEditTransactionEvent.SavingsGoalSheetDismissed -> {
                _uiState.update { it.copy(showSavingsGoalSheet = false) }
            }

            is AddEditTransactionEvent.SavingsGoalSelected -> {
                _uiState.update {
                    it.copy(
                        selectedSavingsGoal = event.goal,
                        showSavingsGoalSheet = false
                    )
                }
            }
        }
    }

    private fun observeNavigationResults() {
        resultManager.result.onEach { resultData ->
            when (resultData) {
                is AccountItem -> onEvent(AddEditTransactionEvent.AccountSelected(resultData))
                is CategoryItem -> onEvent(AddEditTransactionEvent.CategorySelected(resultData))
                is ScanResult -> handleScanResult(resultData)
            }
            if (resultData != null) resultManager.setResult(null)
        }.launchIn(viewModelScope)
    }

    private fun loadTransactionForEdit(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transactionItem = getTransactionByIdUseCase(id).first()

            if (transactionItem != null) {
                val account = getAccountUseCase(transactionItem.accountId)
                val category = transactionItem.categoryId?.let { getCategoryUseCase(it) }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        amount = transactionItem.amount.toPlainString(),
                        selectedTransactionType = transactionItem.type,
                        selectedDate = transactionItem.date.toLocalDate(),
                        selectedAccount = account,
                        selectedCategory = category,
                        descriptions = transactionItem.description
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Transaction not found.") }
            }
        }
    }

    private fun handleScanResult(scanResult: ScanResult) {
        viewModelScope.launch {
            var suggestedCategory: CategoryItem? = null

            // 1. Cari kategori lokal berdasarkan 'key' dari backend
            scanResult.suggestedCategoryKey?.let { key ->
                suggestedCategory = getCategoryByStandardKeyUseCase(key)
            }

            // 2. Update state dengan semua data yang didapat dari hasil scan
            _uiState.update {
                it.copy(
                    // Isi form secara otomatis
                    amount = scanResult.amount?.toPlainString() ?: it.amount,
                    descriptions = scanResult.description ?: it.descriptions,
                    selectedDate = scanResult.date?.toLocalDate() ?: it.selectedDate,
                    selectedCategory = suggestedCategory ?: it.selectedCategory
                )
            }
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true) }

            // Cek tipe transaksi yang dipilih
            if (currentState.selectedTransactionType == TransactionType.SAVINGS) {
                // --- ALUR UNTUK TABUNGAN ---

                // Validasi untuk form tabungan
                if (currentState.selectedAccount == null || currentState.selectedSavingsGoal == null || currentState.amount.isBlank()) {
                    _uiState.update { it.copy(error = "Akun dan Tujuan Tabungan harus diisi.", isLoading = false) }
                    return@launch
                }

                // Transaksi untuk tabungan dicatat sebagai 'Expense'
                // karena mengurangi saldo akun, tetapi dihubungkan ke savingsGoalId.
                val savingsTransaction = TransactionItem(
                    id = editingTransactionId ?: UUID.randomUUID().toString(),
                    description = currentState.descriptions,
                    amount = BigDecimal(currentState.amount),
                    type = TransactionType.EXPENSE,
                    date = currentState.selectedDate.atTime(LocalTime.MAX),
                    categoryId = currentState.selectedCategory?.id, // Gunakan ID & nama dari tujuan tabungan
                    categoryName = currentState.selectedCategory?.name ?: "",
                    categoryIconIdentifier = currentState.selectedSavingsGoal.iconIdentifier,
                    accountId = currentState.selectedAccount.id,
                    accountName = currentState.selectedAccount.name,
                    savingsGoalId = currentState.selectedSavingsGoal.id // Hubungkan ke ID tujuan tabungan
                )

                try {
                    // 1. Catat sebagai transaksi pengeluaran biasa
                    addTransactionUseCase(savingsTransaction)
                    // 2. Tambahkan dana ke tujuan tabungan
                    addFundsToSavingsGoalUseCase(
                        goalId = currentState.selectedSavingsGoal.id,
                        amount = BigDecimal(currentState.amount)
                    )
                    snackbarManager.showMessage("Setoran berhasil ditambahkan")
                    _navigationEvent.emit(Home) // Kembali ke Home
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }

            } else {
                // --- ALUR UNTUK INCOME & EXPENSE (TETAP SAMA) ---

                // Validasi untuk form standar
                if (currentState.selectedAccount == null || currentState.selectedCategory == null || currentState.amount.isBlank()) {
                    _uiState.update { it.copy(error = "Please fill all required fields.", isLoading = false) }
                    return@launch
                }

                val transactionToSave = TransactionItem(
                    id = editingTransactionId ?: UUID.randomUUID().toString(),
                    description = currentState.descriptions,
                    amount = BigDecimal(currentState.amount),
                    type = currentState.selectedTransactionType,
                    date = currentState.selectedDate.atTime(LocalTime.MAX),
                    categoryId = currentState.selectedCategory.id,
                    categoryName = currentState.selectedCategory.name,
                    categoryIconIdentifier = currentState.selectedCategory.iconIdentifier,
                    accountId = currentState.selectedAccount.id,
                    accountName = currentState.selectedAccount.name
                )

                try {
                    if (isEditMode) {
                        updateTransactionUseCase(transactionToSave)
                        snackbarManager.showMessage("Transaction updated successfully")
                    } else {
                        addTransactionUseCase(transactionToSave)
                        snackbarManager.showMessage("Transaction saved successfully")
                    }
                    _navigationEvent.emit(Home) // Kembali ke Home
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }

    fun deleteTransaction() {
        if (isEditMode) {
            _uiState.update { it.copy(showDeleteConfirmDialog = true) }
        }
    }

    fun onConfirmDelete() {
        if (isEditMode) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
                try {
                    deleteTransactionUseCase(editingTransactionId!!)
                    _uiState.update { it.copy(isLoading = false, isTransactionSaved = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteConfirmDialog = false) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date, showDatePicker = false) }
    }

    fun onDatePickerDismissed() {
        _uiState.update { it.copy(showDatePicker = false) }
    }
}