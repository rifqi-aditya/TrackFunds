package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.feature.transaction.ui.model.AddEditTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
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
    savedStateHandle: SavedStateHandle,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val args: AddEditTransaction = savedStateHandle.toRoute()
    private val editingTransactionId: String? = args.transactionId

    val isEditMode: Boolean = editingTransactionId != null

    private val _uiState = MutableStateFlow(AddEditTransactionUiState())
    val uiState: StateFlow<AddEditTransactionUiState> = _uiState.asStateFlow()

    init {
        if (isEditMode) {
            loadTransactionForEdit(editingTransactionId!!)
        }
        observeNavigationResults()
    }

    private fun observeNavigationResults() {
        resultManager.result
            .onEach { resultData ->
                when (resultData) {
                    is AccountItem -> {
                        _uiState.update { it.copy(selectedAccount = resultData) }
                        resultManager.setResult(null) // Bersihkan hasil
                    }

                    is CategoryItem -> {
                        _uiState.update { it.copy(selectedCategory = resultData) }
                        resultManager.setResult(null) // Bersihkan hasil
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTransactionForEdit(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transactionItem = getTransactionByIdUseCase(id).first()

            if (transactionItem != null) {
                // Ambil objek lengkap dari Akun dan Kategori menggunakan UseCase
                val account = getAccountUseCase(transactionItem.accountId)
                val category = transactionItem.categoryId?.let { getCategoryUseCase(it) }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        amount = transactionItem.amount.toPlainString(),
                        selectedTransactionType = transactionItem.type,
                        selectedDate = transactionItem.date.toLocalDate(),
                        selectedAccount = account, // Gunakan objek lengkap
                        selectedCategory = category, // Gunakan objek lengkap
                        notes = transactionItem.note
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Transaction not found.") }
            }
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.selectedAccount == null || currentState.selectedCategory == null || currentState.amount.isBlank()) {
                _uiState.update { it.copy(error = "Please fill all required fields.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val transactionToSave = TransactionItem(
                id = editingTransactionId ?: UUID.randomUUID().toString(),
                note = currentState.notes,
                amount = BigDecimal(currentState.amount),
                type = currentState.selectedTransactionType,
                date = currentState.selectedDate.atStartOfDay(),
                categoryId = currentState.selectedCategory.id,
                categoryName = currentState.selectedCategory.name,
                iconIdentifier = currentState.selectedCategory.iconIdentifier,
                accountId = currentState.selectedAccount.id,
                accountName = currentState.selectedAccount.name
            )

            try {
                if (isEditMode) {
                    updateTransactionUseCase(transactionToSave)
                } else {
                    addTransactionUseCase(transactionToSave)
                }
                _uiState.update { it.copy(isLoading = false, isTransactionSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onDeleteClick() {
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

    fun onAmountChange(amount: String) {
        if (amount.all { it.isDigit() }) {
            _uiState.update { it.copy(amount = amount) }
        }
    }

    fun onTransactionTypeSelected(type: TransactionType) {
        _uiState.update { it.copy(selectedTransactionType = type, selectedCategory = null) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date, showDatePicker = false) }
    }

    fun onDatePickerDismissed() {
        _uiState.update { it.copy(showDatePicker = false) }
    }

    fun onDateSelectorClicked() {
        _uiState.update { it.copy(showDatePicker = true) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetTransactionSavedFlag() {
        _uiState.update { it.copy(isTransactionSaved = false) }
    }
}