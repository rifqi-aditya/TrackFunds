package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.feature.scan.ui.event.ScanEvent
import com.rifqi.trackfunds.feature.scan.ui.state.ScanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onEvent(event: ScanEvent) {
        when (event) {
            is ScanEvent.PhotoTaken -> _uiState.update { it.copy(capturedImageUri = event.uri) }
            is ScanEvent.RetakePhoto -> _uiState.update { it.copy(capturedImageUri = null) }
            is ScanEvent.ConfirmPhoto -> {
                // --- TAMBAHKAN PRINTLN INI ---
                println("✅ 2. ViewModel menerima event ConfirmPhoto")
                processImage()
            }
        }
    }

    private fun processImage() {
        println("✅ 3. Fungsi processImage() dimulai.")

        _uiState.value.capturedImageUri?.let { uri ->
            println("✅ 4. URI gambar ditemukan: $uri. Memulai coroutine...")

            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    println("✅ 5. Memanggil scanReceiptUseCase...")
                    val scanResult = scanReceiptUseCase(uri)
                    println("✅ 6. UseCase selesai. Hasil: $scanResult")

                    resultManager.setResult(scanResult)
                    _uiState.update { it.copy(isLoading = false, isScanSuccessful = true) }
                } catch (e: Exception) {
                    // Jika ada error di backend atau saat upload, ini akan tereksekusi
                    println("❌ ERROR saat memproses gambar: ${e.message}")
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        } ?: println("❌ GAGAL: capturedImageUri ternyata null saat processImage dipanggil!")
    }
}