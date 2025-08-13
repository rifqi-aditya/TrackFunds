package com.rifqi.trackfunds.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.account.usecase.ObserveAccountCountUseCase
import com.rifqi.trackfunds.core.domain.common.repository.UserSessionProvider
import com.rifqi.trackfunds.core.domain.settings.usecase.GetThemePreferenceUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountSetup
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    getThemePreferenceUseCase: GetThemePreferenceUseCase,
    private val userSession: UserSessionProvider,
    private val observeAccountCount: ObserveAccountCountUseCase,
    val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent: SharedFlow<AppScreen> = _navigationEvent.asSharedFlow()

    init {
        getThemePreferenceUseCase()
            .onEach { theme ->
                _uiState.update { it.copy(theme = theme) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val gateFlow: Flow<AppScreen> =
                userSession.getUidFlow()
                    .flatMapLatest { uid ->
                        if (uid == null) {
                            flowOf(Auth)
                        } else {
                            observeAccountCount().map { count ->
                                if (count == 0) AccountSetup else HomeGraph
                            }
                        }
                    }
                    .distinctUntilChanged()

            // Set start destination sekali (untuk cold start)
            val firstDest = gateFlow.first()
            _uiState.update { it.copy(startDestination = firstDest) }

            // Perubahan berikutnya (login/logout, hapus semua akun, dsb) → navigate
            gateFlow.drop(1).collect { dest ->
                _navigationEvent.emit(dest)
            }
        }

    }

    fun onEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.FloatButtonClicked -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = true) }
                }

                MainEvent.AddActionDialogDismissed -> {
                    _uiState.update { it.copy(isAddActionDialogVisible = false) }
                }

                MainEvent.AddTransactionManuallyClicked -> {
                    _navigationEvent.emit(TransactionRoutes.AddEditTransaction())
                }

                MainEvent.ScanReceiptClicked -> {
                    _navigationEvent.emit(ScanGraph)
                }
            }
        }
    }
}
