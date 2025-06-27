package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.CameraScan
import com.rifqi.trackfunds.feature.scan.ui.event.ScanOptionEvent
import com.rifqi.trackfunds.feature.scan.ui.state.ScanOptionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanOptionViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanOptionUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: ScanOptionEvent) {
        when (event) {
            ScanOptionEvent.SelectFromCameraClicked -> {
                viewModelScope.launch {
                    _navigationEvent.emit(CameraScan)
                }
            }

            else -> {}
        }
    }
}