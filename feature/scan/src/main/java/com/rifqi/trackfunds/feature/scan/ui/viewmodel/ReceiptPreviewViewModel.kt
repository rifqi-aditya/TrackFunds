package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.core.navigation.api.ScanRoutes
import com.rifqi.trackfunds.feature.scan.ui.event.ReceiptPreviewEvent
import com.rifqi.trackfunds.feature.scan.ui.state.ReceiptPreviewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("UseKtx")
@HiltViewModel
class ReceiptPreviewViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase,
    private val resultManager: NavigationResultManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState = MutableStateFlow(ReceiptPreviewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Ambil argumen, decode, dan simpan ke dalam state
        val args: ScanRoutes.ReceiptPreview = savedStateHandle.toRoute()
        val decodedUriString = Uri.decode(args.imageUri) // FIX 1: Decode URI string
        val imageUri = decodedUriString.toUri() // FIX 2: Parse menjadi objek Uri
        _uiState.update { it.copy(imageUri = imageUri) } // FIX 3: Simpan ke dalam UiState
    }

    fun onEvent(event: ReceiptPreviewEvent) {
        when (event) {
            ReceiptPreviewEvent.ConfirmScanClicked -> processImage()
        }
    }

    private fun processImage() {
        _uiState.value.imageUri?.let { uri ->
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    val scanResult = scanReceiptUseCase(uri)
                    resultManager.setResult(scanResult)
                    _uiState.update { it.copy(isScanSuccessful = true) }
                } catch (e: Exception) {
                    resultManager.setResult(e)
                    _uiState.update { it.copy(isLoading = false, isScanSuccessful = true) }
                }
            }
        }
    }

}