package com.rifqi.trackfunds.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.event.TrackFundsMainEvent
import com.rifqi.trackfunds.state.TrackFundsMainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackFundsMainViewModel @Inject constructor(
    val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackFundsMainState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

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
                    _navigationEvent.emit(AddEditTransaction())
                }

                TrackFundsMainEvent.ScanReceiptClicked -> {
                    _navigationEvent.emit(ScanGraph)
                }
            }
        }
    }
}