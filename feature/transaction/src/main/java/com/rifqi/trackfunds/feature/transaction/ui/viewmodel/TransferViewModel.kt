package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCase
import com.rifqi.trackfunds.feature.transaction.ui.event.TransferEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.AccountSelectionMode
import com.rifqi.trackfunds.feature.transaction.ui.model.TransferUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val performTransferUseCase: PerformTransferUseCase,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    init {
        observeNavigationResult()
    }

    private fun observeNavigationResult() {
        resultManager.result.onEach { result ->
            if (result is AccountItem) {
                println("MENERIMA DARI KURIR: ID $result")
                // Cek mode yang tersimpan di state untuk menentukan target
                when (_uiState.value.accountSelectionMode) {
                    AccountSelectionMode.FROM -> {
                        println("MODE DIBACA: FROM. Memproses...")
                        onEvent(TransferEvent.FromAccountSelected(result))
                    }

                    AccountSelectionMode.TO -> {
                        println("MODE DIBACA: TO. Memproses...")
                        onEvent(TransferEvent.ToAccountSelected(result))
                    }

                    AccountSelectionMode.NONE -> {
                        println("MODE DIBACA: NONE. Hasil diabaikan.")
                    }
                }
                resultManager.setResult(null)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: TransferEvent) {
        when (event) {
            // Logika untuk menangani hasil dan MERESET mode pemilihan
            is TransferEvent.FromAccountSelected -> _uiState.update {
                it.copy(
                    fromAccount = event.account,
                    accountSelectionMode = AccountSelectionMode.NONE
                )
            }

            is TransferEvent.ToAccountSelected -> _uiState.update {
                it.copy(toAccount = event.account, accountSelectionMode = AccountSelectionMode.NONE)
            }

            // Logika untuk MENGATUR mode pemilihan sebelum navigasi
            TransferEvent.SelectFromAccountClicked -> {
                _uiState.update { it.copy(accountSelectionMode = AccountSelectionMode.FROM) }
                println("MODE SET: FROM")
            }

            TransferEvent.SelectToAccountClicked -> {
                _uiState.update { it.copy(accountSelectionMode = AccountSelectionMode.TO) }
                println("MODE SET: TO")
            }

            // Event handler untuk form
            is TransferEvent.AmountChanged -> if (event.amount.all { it.isDigit() }) {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is TransferEvent.NoteChanged -> _uiState.update { it.copy(note = event.note) }
            is TransferEvent.DateChanged -> _uiState.update {
                it.copy(
                    date = event.date,
                    showDatePicker = false
                )
            }

            TransferEvent.DatePickerDismissed -> _uiState.update { it.copy(showDatePicker = false) }
            TransferEvent.DateSelectorClicked -> _uiState.update { it.copy(showDatePicker = true) } // Event handler baru

            // Aksi utama
            TransferEvent.PerformTransferClicked -> performTransfer()
        }
    }

    private fun performTransfer() {
        viewModelScope.launch {
            val state = _uiState.value

            // Validasi yang lebih baik
            if (state.fromAccount == null || state.toAccount == null) {
                _uiState.update { it.copy(error = "Please select source and destination accounts.") }
                return@launch
            }
            if (state.fromAccount.id == state.toAccount.id) {
                _uiState.update { it.copy(error = "Source and destination accounts cannot be the same.") }
                return@launch
            }
            if (state.amount.isBlank() || BigDecimal(state.amount) <= BigDecimal.ZERO) {
                _uiState.update { it.copy(error = "Amount must be greater than zero.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            try {
                performTransferUseCase(
                    fromAccount = state.fromAccount,
                    toAccount = state.toAccount,
                    amount = BigDecimal(state.amount),
                    date = state.date.atStartOfDay(),
                    note = state.note
                )
                _uiState.update { it.copy(isLoading = false, isTransferSuccessful = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}