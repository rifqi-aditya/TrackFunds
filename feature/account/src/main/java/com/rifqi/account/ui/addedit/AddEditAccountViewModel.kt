package com.rifqi.account.ui.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.account.model.AddAccountParams
import com.rifqi.trackfunds.core.domain.account.model.UpdateAccountParams
import com.rifqi.trackfunds.core.domain.account.usecase.AddAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountByIdUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.UpdateAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.ValidateAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val validateAccountUseCase: ValidateAccountUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val accountId: String? = savedStateHandle["accountId"]
    val isEditMode: Boolean = accountId != null

    private val _uiState = MutableStateFlow(AddEditAccountUiState())
    val uiState: StateFlow<AddEditAccountUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<AddEditAccountSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        if (isEditMode) {
            loadAccountData(accountId!!)
        }
    }

    fun onEvent(event: AddEditAccountEvent) {
        when (event) {
            is AddEditAccountEvent.NameChanged -> _uiState.update {
                it.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is AddEditAccountEvent.BalanceChanged -> _uiState.update {
                it.copy(
                    initialBalance = event.balance,
                    balanceError = null
                )
            }

            is AddEditAccountEvent.IconIdentifierChanged -> _uiState.update {
                it.copy(
                    iconIdentifier = event.identifier,
                    showIconPicker = false,
                    iconError = null
                )
            }

            AddEditAccountEvent.ShowIconPickerClicked -> _uiState.update { it.copy(showIconPicker = true) }
            AddEditAccountEvent.IconPickerDismissed -> _uiState.update { it.copy(showIconPicker = false) }
            AddEditAccountEvent.SaveAccountClicked -> saveAccount()
            AddEditAccountEvent.DeleteClicked -> _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            AddEditAccountEvent.ConfirmDeleteClicked -> deleteAccount()
            AddEditAccountEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }
        }
    }

    private fun loadAccountData(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getAccountByIdUseCase(id)
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = account.name,
                            initialBalance = account.balance.toPlainString(), // Saldo awal tidak di-load untuk edit
                            iconIdentifier = account.iconIdentifier
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun saveAccount() {
        val currentState = _uiState.value

        // Panggil UseCase validasi
        val validationResult = validateAccountUseCase(
            name = currentState.name,
            balance = currentState.initialBalance,
            icon = currentState.iconIdentifier,
            isEditMode = isEditMode
        )

        if (!validationResult.isSuccess) {
            _uiState.update {
                it.copy(
                    nameError = validationResult.nameError,
                    balanceError = validationResult.balanceError,
                    iconError = validationResult.iconError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val result: Result<Unit> = if (isEditMode) {
                val params = UpdateAccountParams(
                    id = accountId!!,
                    name = currentState.name,
                    iconIdentifier = currentState.iconIdentifier
                )
                updateAccountUseCase(params)
            } else {
                val params = AddAccountParams(
                    name = currentState.name,
                    initialBalance = currentState.initialBalance.toBigDecimal(),
                    iconIdentifier = currentState.iconIdentifier
                )
                addAccountUseCase(params)
            }

            result.onSuccess {
                val message = if (isEditMode) "Account updated" else "Account created"
                _sideEffect.send(AddEditAccountSideEffect.ShowSnackbar(message))
                _sideEffect.send(AddEditAccountSideEffect.NavigateBack)
            }.onFailure { exception ->
                _uiState.update { it.copy(isSaving = false, error = exception.message) }
            }
        }
    }

    private fun deleteAccount() {
        if (accountId == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, showDeleteConfirmDialog = false) }
            deleteAccountUseCase(accountId)
                .onSuccess {
                    _sideEffect.send(AddEditAccountSideEffect.ShowSnackbar("Account deleted"))
                    _sideEffect.send(AddEditAccountSideEffect.NavigateBack)
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isSaving = false, error = exception.message) }
                }
        }
    }
}