package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.account.ui.event.AddEditAccountEvent
import com.rifqi.account.ui.state.AddEditAccountUiState
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.usecase.account.AddAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.UpdateAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditAccountViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val accountId: String? = savedStateHandle["accountId"]

    private val _uiState = MutableStateFlow(AddEditAccountUiState())
    val uiState = _uiState.asStateFlow()

    init {
        if (accountId != null) {
            loadAccountData(accountId)
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

            is AddEditAccountEvent.BalanceChanged -> _uiState.update { it.copy(initialBalance = event.balance) }
            is AddEditAccountEvent.IconChanged -> _uiState.update { it.copy(iconIdentifier = event.iconIdentifier) }
            AddEditAccountEvent.SaveAccountClicked -> saveAccount()
            AddEditAccountEvent.DeleteClicked -> _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            AddEditAccountEvent.DismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }

            AddEditAccountEvent.ConfirmDeleteClicked -> deleteAccount()
            AddEditAccountEvent.ShowIconPickerClicked -> _uiState.update {
                it.copy(showIconPicker = true)
            }

            is AddEditAccountEvent.IconIdentifierChanged -> _uiState.update {
                it.copy(iconIdentifier = event.identifier)
            }

            AddEditAccountEvent.IconPickerDismissed -> {
                _uiState.update { it.copy(showIconPicker = false) }
            }
        }
    }

    private fun loadAccountData(id: String) {
        viewModelScope.launch {
            getAccountByIdUseCase(id)
                .onSuccess { account ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditMode = true,
                            screenTitle = "Edit Account",
                            name = account.name,
                            initialBalance = account.balance.toPlainString(),
                            iconIdentifier = account.iconIdentifier ?: "",
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun saveAccount() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.name.isBlank()) {
                _uiState.update { it.copy(nameError = "Account name cannot be empty") }
                return@launch
            }

            _uiState.update { it.copy(isSaving = true) }

            val account = AccountModel(
                id = accountId ?: UUID.randomUUID().toString(),
                name = currentState.name,
                balance = currentState.initialBalance.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                iconIdentifier = currentState.iconIdentifier
            )

            try {
                if (currentState.isEditMode) {
                    updateAccountUseCase(account)
                } else {
                    addAccountUseCase(account)
                }
                _uiState.update { it.copy(isSaving = false, isAccountSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(showDeleteConfirmDialog = false) }
            accountId?.let {
                try {
                    deleteAccountUseCase(it)
                    _uiState.update { it.copy(isAccountSaved = true) } // Memicu navigasi kembali
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = e.message) }
                }
            }
        }
    }
}