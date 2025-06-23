package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.account.ui.model.AccountSummaryItem
import com.rifqi.account.ui.model.AccountsUiState
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountsUiState(isLoading = true))
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    init {
        loadAccountsData()
    }

    private fun loadAccountsData() {
        viewModelScope.launch {
            getAccountsUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            // Anda bisa menambahkan properti 'error' pada UiState
                            // error = exception.message
                        )
                    }
                }
                .collect { accountsFromUseCase ->
                    val accountsForUi = accountsFromUseCase.map { domainAccount ->
                        AccountSummaryItem(
                            id = domainAccount.id,
                            name = domainAccount.name,
                            balance = domainAccount.balance,
                            iconIdentifier = "wallet_account"
                        )
                    }

                    val totalBalance = accountsFromUseCase.sumOf { it.balance }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalBalance = totalBalance,
                            accounts = accountsForUi
                        )
                    }
                }
        }
    }
}