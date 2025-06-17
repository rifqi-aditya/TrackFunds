package com.rifqi.add_transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.add_transaction.ui.model.AddTransactionUiState
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val savedStateHandle: SavedStateHandle,
    resultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        resultManager.result
            .onEach { resultData ->
                if (resultData is AccountItem) {
                    _uiState.update { it.copy(selectedAccount = resultData) }
                    resultManager.setResult(null)
                }

                if (resultData is CategoryItem) {
                    _uiState.update { it.copy(selectedCategory = resultData) }
                    resultManager.setResult(null)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAmountChange(amount: String) {
        if (amount.all { it.isDigit() }) {
            _uiState.update { it.copy(amount = amount) }
        }
    }

    fun onTransactionTypeSelected(type: TransactionType) {
        _uiState.update { it.copy(selectedTransactionType = type, selectedCategory = null) }
    }

    fun onAccountSelected(account: AccountItem) {
        _uiState.update { it.copy(selectedAccount = account) }
    }

    fun onCategorySelected(category: CategoryItem) {
        _uiState.update { it.copy(selectedCategory = category) }
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

    fun onSaveTransaction() {
        val currentState = _uiState.value

        // Validasi
        if (currentState.amount.isBlank() || BigDecimal(currentState.amount) <= BigDecimal.ZERO) {
            _uiState.update { it.copy(error = "Jumlah transaksi harus diisi dan lebih dari nol.") }
            return
        }
        if (currentState.selectedAccount == null) {
            _uiState.update { it.copy(error = "Harap pilih akun.") }
            return
        }
        if (currentState.selectedCategory == null) {
            _uiState.update { it.copy(error = "Harap pilih kategori.") }
            return
        }

        // Buat objek domain dari state UI
        val transactionToSave = TransactionItem(
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

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                addTransactionUseCase(transactionToSave)
                _uiState.update { it.copy(isLoading = false, isTransactionSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Terjadi kesalahan saat menyimpan."
                    )
                }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetTransactionSavedFlag() {
        _uiState.update { it.copy(isTransactionSaved = false) }
    }
}