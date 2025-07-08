package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCase
import com.rifqi.trackfunds.feature.transaction.ui.event.TransferEvent
import com.rifqi.trackfunds.feature.transaction.ui.state.AccountSelectionMode
import com.rifqi.trackfunds.feature.transaction.ui.state.TransferUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val performTransferUseCase: PerformTransferUseCase,
    private val resultManager: NavigationResultManager,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    init {
        observeNavigationResult()
    }

    private fun observeNavigationResult() {
        resultManager.result.onEach { result ->
            if (result is AccountItem) {
                when (_uiState.value.accountSelectionMode) {
                    AccountSelectionMode.FROM -> {
                        onEvent(TransferEvent.FromAccountSelected(result))
                    }

                    AccountSelectionMode.TO -> {
                        onEvent(TransferEvent.ToAccountSelected(result))
                    }

                    AccountSelectionMode.NONE -> {
                    }
                }
                resultManager.setResult(null)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: TransferEvent) {
        when (event) {
            is TransferEvent.FromAccountSelected -> _uiState.update {
                it.copy(
                    fromAccount = event.account,
                    accountSelectionMode = AccountSelectionMode.NONE
                )
            }

            is TransferEvent.ToAccountSelected -> _uiState.update {
                it.copy(toAccount = event.account, accountSelectionMode = AccountSelectionMode.NONE)
            }

            TransferEvent.SelectFromAccountClicked -> {
                _uiState.update { it.copy(accountSelectionMode = AccountSelectionMode.FROM) }
            }

            TransferEvent.SelectToAccountClicked -> {
                _uiState.update { it.copy(accountSelectionMode = AccountSelectionMode.TO) }
            }

            is TransferEvent.AmountChanged -> if (event.amount.all { it.isDigit() }) {
                _uiState.update { it.copy(amount = event.amount) }
            }

            is TransferEvent.descriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is TransferEvent.DateChanged -> _uiState.update {
                it.copy(
                    date = event.date,
                    showDatePicker = false
                )
            }

            TransferEvent.DatePickerDismissed -> _uiState.update { it.copy(showDatePicker = false) }
            TransferEvent.DateSelectorClicked -> _uiState.update { it.copy(showDatePicker = true) } // Event handler baru

            TransferEvent.PerformTransferClicked -> performTransfer()
        }
    }

    private fun performTransfer() {
        viewModelScope.launch {
            val state = _uiState.value

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

            val transferId = UUID.randomUUID().toString()
            val date = state.date.atTime(LocalTime.now())
            val amount = BigDecimal(state.amount)

            val expense = TransactionItem(
                description = "Transfer to ${state.toAccount.name}${if (state.description.isNotBlank()) ": ${state.description}" else ""}",
                amount = amount,
                type = TransactionType.EXPENSE,
                date = date,
                account = state.fromAccount,
                transferPairId = transferId
            )
            val income = TransactionItem(
                description = "Transfer from ${state.fromAccount.name}${if (state.description.isNotBlank()) ": ${state.description}" else ""}",
                amount = amount,
                type = TransactionType.INCOME,
                date = date,
                account = state.toAccount,
                transferPairId = transferId
            )

            try {
                // Panggil UseCase dengan objek yang sudah jadi
                performTransferUseCase(expense, income)
                snackbarManager.showMessage("Transfer berhasil")
                _uiState.update { it.copy(isLoading = false, isTransferSuccessful = true) }
            } catch (e: Exception) {
                snackbarManager.showMessage("Transfer gagal: ${e.message}")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}