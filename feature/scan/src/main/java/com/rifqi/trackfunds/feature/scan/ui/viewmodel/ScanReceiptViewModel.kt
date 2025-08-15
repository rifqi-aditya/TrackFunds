package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.common.utils.ReceiptImageStore
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.common.exception.ScanException
import com.rifqi.trackfunds.core.domain.scan.usecase.ScanReceiptUseCase
import com.rifqi.trackfunds.feature.scan.ui.event.ScanReceiptEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.ScanReceiptSideEffect
import com.rifqi.trackfunds.feature.scan.ui.state.ScanPhase
import com.rifqi.trackfunds.feature.scan.ui.state.ScanReceiptUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
    private val navigationResultManager: NavigationResultManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanReceiptUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<ScanReceiptSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeCameraResult()
        viewModelScope.launch {
            val allAccounts = getAccountsUseCase().first()
            _uiState.update { it.copy(allAccounts = allAccounts) }
        }
    }

    fun onEvent(event: ScanReceiptEvent) {
        when (event) {
            is ScanReceiptEvent.ImageSelected -> {
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                        ReceiptImageStore.saveIntoAppStorage(appContext, event.uri)
                    }.onSuccess { stored ->
                        _uiState.update {
                            it.copy(
                                currentPhase = ScanPhase.IMAGE_PREVIEW,
                                imagePreviewUri = stored,
                                errorMessage = null
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update { it.copy(errorMessage = e.message) }
                    }
                }
            }

            is ScanReceiptEvent.ConfirmImage -> {
                uiState.value.imagePreviewUri?.let { stableUri ->
                    processImage(stableUri)
                }
            }

            ScanReceiptEvent.ScanReceiptAgainClicked -> {
                _uiState.update {
                    it.copy(currentPhase = ScanPhase.UPLOAD, imagePreviewUri = null, errorMessage = null)
                }
            }

            ScanReceiptEvent.SelectFromCameraClicked -> launchSideEffect(ScanReceiptSideEffect.NavigateToCamera)
            ScanReceiptEvent.SelectFromGalleryClicked -> launchSideEffect(ScanReceiptSideEffect.LaunchGallery)
        }
    }

    private fun launchSideEffect(effect: ScanReceiptSideEffect) {
        viewModelScope.launch { _sideEffect.send(effect) }
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
                    val withStableUri = scanResult.copy(receiptImageUri = uiState.value.imagePreviewUri)
                    navigationResultManager.setResult(withStableUri)
                    _sideEffect.send(ScanReceiptSideEffect.NavigateToAddTransaction)
                }
                .onFailure { error ->
                    val msg = when (error) {
                        is ScanException.NoTextFound,
                        is ScanException.NetworkError,
                        is ScanException.ParsingFailed,
                        is ScanException.UnknownError,
                        is ScanException.NotAReceipt -> error.message
                        else -> "An unexpected error occurred. Please try again."
                    }
                    _uiState.update { it.copy(currentPhase = ScanPhase.IMAGE_PREVIEW, errorMessage = msg) }
                }
        }
    }
}
