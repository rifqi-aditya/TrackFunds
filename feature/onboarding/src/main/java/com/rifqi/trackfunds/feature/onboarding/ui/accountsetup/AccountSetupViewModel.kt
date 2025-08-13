package com.rifqi.trackfunds.feature.onboarding.ui.accountsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.account.model.AccountsIcons
import com.rifqi.trackfunds.core.domain.account.model.AddAccountParams
import com.rifqi.trackfunds.core.domain.account.usecase.AddAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AccountSetupViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AccountSetupUiState(
            availableIcons = AccountsIcons.list,
            selectedIcon = AccountsIcons.list.firstOrNull()
        )
    )
    val uiState: StateFlow<AccountSetupUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<AccountSetupSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<AccountSetupSideEffect> = _sideEffect.receiveAsFlow()

    fun onEvent(event: AccountSetupEvent) {
        when (event) {
            AccountSetupEvent.IconPickerOpened ->
                update { it.copy(isIconSheetVisible = true) }

            AccountSetupEvent.IconPickerDismissed ->
                update { it.copy(isIconSheetVisible = false) }

            is AccountSetupEvent.IconSelected ->
                update { it.copy(selectedIcon = event.key, isIconSheetVisible = false) }

            is AccountSetupEvent.NameChanged ->
                update { it.copy(name = event.value, nameError = null) }

            is AccountSetupEvent.InitialBalanceChanged ->
                update { it.copy(initialBalance = event.value, balanceError = null) }

            AccountSetupEvent.SubmitClicked -> submit()
        }
    }

    private fun submit() {
        val s = _uiState.value
        val name = s.name.trim()
        if (name.isBlank()) {
            update { it.copy(nameError = "Nama akun wajib diisi") }
            return
        }
        val amount = s.initialBalance.toBigDecimal()
        if (amount < BigDecimal.ZERO) {
            update { it.copy(balanceError = "Saldo awal tidak boleh negatif") }
            return
        }

        viewModelScope.launch {
            update { it.copy(isSaving = true) }
            runCatching {
                addAccountUseCase(
                    AddAccountParams(
                        name = name,
                        initialBalance = amount,
                        iconIdentifier = s.selectedIcon ?: "wallet_account"
                    )
                )
            }.onSuccess {
                _sideEffect.send(AccountSetupSideEffect.NavigateHome)
            }.onFailure { e ->
                _sideEffect.send(
                    AccountSetupSideEffect.ShowMessage(
                        e.message ?: "Error occurred while saving account"
                    )
                )
            }
            update { it.copy(isSaving = false) }
        }
    }

    private inline fun update(block: (AccountSetupUiState) -> AccountSetupUiState) {
        _uiState.update(block)
    }
}