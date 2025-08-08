package com.rifqi.trackfunds.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.settings.usecase.GetThemePreferenceUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getThemePreferenceUseCase: GetThemePreferenceUseCase,
    val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        getThemePreferenceUseCase()
            .onEach { theme -> _uiState.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: TrackFundsMainEvent) {
        viewModelScope.launch {
            when (event) {
                is TrackFundsMainEvent.FloatButtonClicked -> {
                    _uiState.value = _uiState.value.copy(
                        isAddActionDialogVisible = true
                    )
                }

                TrackFundsMainEvent.AddActionDialogDismissed -> {
                    _uiState.value = _uiState.value.copy(
                        isAddActionDialogVisible = false
                    )
                }

                TrackFundsMainEvent.AddTransactionManuallyClicked -> {
                    _navigationEvent.emit(TransactionRoutes.AddEditTransaction())
                }

                TrackFundsMainEvent.ScanReceiptClicked -> {
                    _navigationEvent.emit(ScanGraph)
                }
            }
        }
    }
}