package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.feature.scan.ui.event.CameraEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.CameraSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<CameraSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.PhotoTaken -> {
                // Set hasil ke result manager
                resultManager.setResult(event.uri)
                // Kirim sinyal untuk navigasi kembali
                viewModelScope.launch {
                    _sideEffect.emit(CameraSideEffect.NavigateBack)
                }
            }
        }
    }
}