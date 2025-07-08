package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionDetailEvent
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: TransactionDetail = savedStateHandle.toRoute()
    val transactionId: String = args.transactionId

    private val _uiState = MutableStateFlow(TransactionDetailUiState())
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadTransactionDetails()
    }

    private fun loadTransactionDetails() {
        viewModelScope.launch {
            getTransactionByIdUseCase(transactionId)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactionItem ->
                    _uiState.update { it.copy(isLoading = false, transaction = transactionItem) }
                }
        }
    }

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            TransactionDetailEvent.EditClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(AddEditTransaction(transactionId = transactionId))
                }
            }

            TransactionDetailEvent.DeleteClicked -> {
                _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            }

            TransactionDetailEvent.ConfirmDeleteClicked -> {
                deleteTransaction()
            }

            TransactionDetailEvent.DismissDeleteDialog -> {
                _uiState.update { it.copy(showDeleteConfirmDialog = false) }
            }
        }
    }

    private fun deleteTransaction() {
        val transactionToDelete = _uiState.value.transaction ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
            try {
                deleteTransactionUseCase(transactionToDelete)
                _navigationEvent.emit(Home)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}