package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.account.ui.event.AccountsEvent
import com.rifqi.account.ui.state.AccountsUiState
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    val uiState: StateFlow<AccountsUiState> = getAccountsUseCase()
        .map { accounts ->
            AccountsUiState(
                isLoading = false,
                accounts = accounts,
                totalBalance = accounts.sumOf { it.balance }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AccountsUiState(isLoading = true)
        )

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AccountsEvent) {
        viewModelScope.launch {
            when (event) {
                is AccountsEvent.AccountClicked -> {
                    _navigationEvent.emit(AccountRoutes.AddEditAccount(accountId = event.accountId))
                }

                AccountsEvent.TransferClicked -> {
                    // _navigationEvent.emit(Transfer)
                }

                AccountsEvent.AddAccountClicked -> {
                    _navigationEvent.emit(AccountRoutes.AddEditAccount())
                }
            }
        }
    }
}