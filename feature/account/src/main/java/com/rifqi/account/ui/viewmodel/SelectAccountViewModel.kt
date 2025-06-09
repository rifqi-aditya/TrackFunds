package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.ui.model.SelectionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// UI State untuk layar ini, hanya berisi daftar item untuk ditampilkan
data class AccountListUiState(
    val isLoading: Boolean = true,
    val selectionItems: List<SelectionItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SelectAccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    // Konversi Flow<List<AccountItem>> dari domain menjadi StateFlow<AccountListUiState> untuk UI
    val uiState: StateFlow<AccountListUiState> =
        getAccountsUseCase().map { accountList -> // 1. Panggil use case
            // 2. Map dari model domain (AccountItem) ke model UI (SelectionItem)
            val selectionItems = accountList.map { account ->
                SelectionItem(
                    id = account.id,
                    name = account.name,
                    description = "1233445556",
                    iconIdentifier = account.iconIdentifier
                )
            }
            // 3. Buat UI State yang sudah berhasil
            AccountListUiState(isLoading = false, selectionItems = selectionItems)
        }
            .catch { exception ->
                // 4. Jika ada error, buat UI State untuk error
                emit(AccountListUiState(isLoading = false, error = exception.message ?: "An unknown error occurred"))
            }
            .stateIn(
                scope = viewModelScope,
                // 5. Atur state awal sebagai loading
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AccountListUiState(isLoading = true)
            )
}