package com.rifqi.trackfunds.feature.transaction.ui.detail

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionItem
import com.rifqi.trackfunds.core.domain.transaction.usecase.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCase
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.transaction.ui.detail.TransactionDetailSideEffect.NavigateToEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val getTransactionDetailsUseCase: GetTransactionDetailsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: TransactionRoutes.TransactionDetail = savedStateHandle.toRoute()
    private val transactionId: String = args.transactionId

    private val _uiState = MutableStateFlow(TransactionDetailUiState())
    val uiState: StateFlow<TransactionDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<TransactionDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadTransactionDetails()
    }

    private fun loadTransactionDetails() {
        getTransactionDetailsUseCase(transactionId)
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .map { transaction -> transaction?.toDetailsUiModel() }
            .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
            .onEach { detailsModel ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        details = detailsModel,
                        error = if (detailsModel == null && !it.isLoading) "Transaction not found." else null
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: TransactionDetailEvent) {
        when (event) {
            TransactionDetailEvent.EditClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(NavigateToEdit(transactionId))
                }
            }

            TransactionDetailEvent.DeleteClicked -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = true
                )
            }

            TransactionDetailEvent.ConfirmDeleteClicked -> deleteTransaction()
            TransactionDetailEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }

            is TransactionDetailEvent.ReceiptClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(TransactionDetailSideEffect.ViewReceipt(event.imageUrl))
                }
            }
        }
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
            deleteTransactionUseCase(transactionId)
                .onSuccess {
                    _sideEffect.send(TransactionDetailSideEffect.ShowSnackbar("Transaction deleted"))
                    _sideEffect.send(TransactionDetailSideEffect.NavigateBackAfterDelete)
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }
}

private fun Transaction.toDetailsUiModel(): TransactionDetailsUiModel {
    val isExpense = this.type == TransactionType.EXPENSE
    val amountColor = if (isExpense) Color.Red else Color(0xFF2E7D32)

    return TransactionDetailsUiModel(
        formattedAmount = formatCurrency(this.amount),
        amountColor = amountColor,
        description = this.description,
        categoryName = this.category?.name ?: "Uncategorized",
        categoryIconIdentifier = this.category?.iconIdentifier,
        formattedDate = this.date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm")),
        accountName = this.account.name,
        transactionType = this.type.name.lowercase().replaceFirstChar { it.titlecase() },
        lineItems = this.items.map { it.toLineItemUiModel() },
        receiptImageUrl = this.receiptImageUrl
    )
}

private fun TransactionItem.toLineItemUiModel(): TransactionItemUiModel {
    return TransactionItemUiModel(
        name = this.name,
        quantityAndPrice = "(x${this.quantity} @ ${formatCurrency(this.price)})",
        formattedTotal = formatCurrency(this.total)
    )
}