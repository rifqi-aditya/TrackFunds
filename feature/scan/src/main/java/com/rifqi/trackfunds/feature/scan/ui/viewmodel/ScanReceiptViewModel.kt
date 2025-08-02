package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.common.exception.ScanException
import com.rifqi.trackfunds.core.domain.scan.usecase.ScanReceiptUseCase
import com.rifqi.trackfunds.feature.scan.ui.event.ScanReceiptEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.ScanReceiptSideEffect
import com.rifqi.trackfunds.feature.scan.ui.state.ScanPhase
import com.rifqi.trackfunds.feature.scan.ui.state.ScanReceiptUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanReceiptViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val navigationResultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanReceiptUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<ScanReceiptSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeCameraResult()
        viewModelScope.launch {
            val allAccounts = getAccountsUseCase().first()
            _uiState.update {
                it.copy(
                    allAccounts = allAccounts,
                )
            }
        }
    }

    fun onEvent(event: ScanReceiptEvent) {
        viewModelScope.launch {
            when (event) {
                is ScanReceiptEvent.ImageSelected -> {
                    _uiState.update {
                        it.copy(
                            currentPhase = ScanPhase.IMAGE_PREVIEW,
                            imagePreviewUri = event.uri
                        )
                    }
                }

                is ScanReceiptEvent.ConfirmImage -> {
                    _uiState.value.imagePreviewUri?.let { uri ->
                        processImage(uri)
                    }
                }

                ScanReceiptEvent.ScanReceiptAgainClicked -> _uiState.update {
                    it.copy(currentPhase = ScanPhase.UPLOAD, scanResult = null, errorMessage = null)
                }

                ScanReceiptEvent.SelectFromCameraClicked -> {
                    _sideEffect.send(ScanReceiptSideEffect.NavigateToCamera)
                }

                ScanReceiptEvent.SelectFromGalleryClicked -> {
                    _sideEffect.send(ScanReceiptSideEffect.LaunchGallery)
                }
            }
        }
    }

    private fun observeCameraResult() {
        navigationResultManager.result
            .onEach { result ->
                if (result is Uri) {
                    onEvent(ScanReceiptEvent.ImageSelected(result))
                    navigationResultManager.setResult(null)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun processImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentPhase = ScanPhase.PROCESSING, errorMessage = null) }

            scanReceiptUseCase(uri)
                .onSuccess { scanResult ->
                    navigationResultManager.setResult(scanResult)
                    _sideEffect.send(ScanReceiptSideEffect.NavigateToAddTransaction)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        is ScanException.NoTextFound -> error.message
                        is ScanException.NetworkError -> error.message
                        is ScanException.ParsingFailed -> error.message
                        is ScanException.UnknownError -> error.message
                        is ScanException.NotAReceipt -> error.message
                        else -> "An unexpected error occurred. Please try again."
                    }

                    _uiState.update {
                        it.copy(
                            currentPhase = ScanPhase.IMAGE_PREVIEW,
                            errorMessage = errorMessage
                        )
                    }
                }
        }
    }
}