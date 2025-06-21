package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.ui.model.SelectionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AccountListUiState(
    val isLoading: Boolean = true,
    val accounts: List<AccountItem> = emptyList(),
    val selectionItems: List<SelectionItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SelectAccountViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    val uiState: StateFlow<AccountListUiState> =
        getAccountsUseCase().map { accountList -> // 1. Panggil use case, dapatkan List<AccountItem>
            // 2. Map dari model domain (AccountItem) ke model UI (SelectionItem)
            val selectionItems = accountList.map { account ->
                SelectionItem(
                    id = account.id,
                    name = account.name,
                    description = account.name,
                    iconIdentifier = account.iconIdentifier
                )
            }
            AccountListUiState(
                isLoading = false,
                accounts = accountList,
                selectionItems = selectionItems
            )
        }
            .catch { exception ->
                emit(
                    AccountListUiState(
                        isLoading = false,
                        error = exception.message ?: "An unknown error occurred"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AccountListUiState(isLoading = true)
            )

    fun onAccountSelected(account: AccountItem) {
        resultManager.setResult(account)
    }
}