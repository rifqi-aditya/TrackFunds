package com.rifqi.account.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.account.usecase.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase // Tambahkan dependensi ini
) : ViewModel() {

    val uiState: StateFlow<AccountsUiState> = getAccountsUseCase()
        .map { accounts ->
            val (emptyTitle, emptyMessage) = if (accounts.isEmpty()) {
                "No Accounts Found" to "Tap the '+' button to add your first account."
            } else {
                null to null
            }

            // Map dari List<Account> ke List<AccountItemUiModel>
            AccountsUiState(
                isLoading = false,
                accounts = accounts.map { it.toUiModel() },
                formattedTotalBalance = formatCurrency(accounts.sumOf { it.balance }),
                emptyStateTitle = emptyTitle,
                emptyStateMessage = emptyMessage
            )
        }
        .catch { e ->
            emit(AccountsUiState(isLoading = false, error = e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AccountsUiState(isLoading = true)
        )

    private val _sideEffect = Channel<AccountsSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: AccountsEvent) {
        viewModelScope.launch {
            when (event) {
                is AccountsEvent.AccountClicked -> {
                    _sideEffect.send(AccountsSideEffect.NavigateToEditAccount(event.accountId))
                }

                is AccountsEvent.AddAccountClicked -> {
                    _sideEffect.send(AccountsSideEffect.NavigateToAddAccount)
                }

                is AccountsEvent.DeleteAccountConfirmed -> {
                    deleteAccount(event.accountId)
                }
            }
        }
    }

    private suspend fun deleteAccount(accountId: String) {
        deleteAccountUseCase(accountId) // Panggil use case
            .onSuccess {
                _sideEffect.send(AccountsSideEffect.ShowSnackbar("Account deleted"))
            }
            .onFailure { error ->
                _sideEffect.send(
                    AccountsSideEffect.ShowSnackbar(
                        error.message ?: "Failed to delete"
                    )
                )
            }
    }
}