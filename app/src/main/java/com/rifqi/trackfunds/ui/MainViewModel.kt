package com.rifqi.trackfunds.ui

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.snackbar.SnackbarManager
import com.rifqi.trackfunds.core.domain.account.usecase.ObserveAccountCountUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveActiveUserIdUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveAppThemeUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveLocaleUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountSetup
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    observeAppThemeUseCase: ObserveAppThemeUseCase,
    observeActiveUserIdUseCase: ObserveActiveUserIdUseCase,
    observeLocaleUseCase: ObserveLocaleUseCase,
    private val observeAccountCount: ObserveAccountCountUseCase,

    val snackbarManager: SnackbarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent: SharedFlow<AppScreen> = _navigationEvent.asSharedFlow()

    init {

        viewModelScope.launch {
            // Ambil nilai bahasa PERTAMA yang valid (bukan string kosong) dari DataStore
            val savedTag = observeLocaleUseCase().first { it.isNotBlank() }

            // Jika nilai yang tersimpan berbeda dari yang diterapkan sistem saat ini
            if (AppCompatDelegate.getApplicationLocales().toLanguageTags() != savedTag) {
                // Terapkan kembali bahasa yang benar
                val locales = LocaleListCompat.forLanguageTags(savedTag)
                AppCompatDelegate.setApplicationLocales(locales)
                Log.d("LocaleCheck", "WORKAROUND: Re-applying locale '$savedTag' on startup.")
            }
        }

        observeAppThemeUseCase()
            .onEach { theme -> _uiState.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            // 1. MULAI kedua operasi database secara bersamaan.
            // Keduanya akan berjalan di background tanpa saling menunggu.
            val userIdJob = async { observeActiveUserIdUseCase().first() }
            val accountCountJob = async { observeAccountCount().first() }

            // 2. TUNGGU hingga keduanya selesai dan ambil hasilnya.
            // Waktu tunggu total adalah waktu operasi terlama, bukan jumlah keduanya.
            val uid = userIdJob.await()
            val count = accountCountJob.await()

            // 3. TENTUKAN halaman tujuan berdasarkan hasil yang sudah siap.
            // Proses ini sangat cepat karena tidak ada lagi akses database.
            val startDest = if (uid.isNullOrEmpty()) {
                Auth
            } else {
                if (count == 0) AccountSetup else HomeGraph
            }

            // 4. UPDATE UI state agar splash screen hilang dan halaman utama muncul.
            _uiState.update { it.copy(startDestination = startDest) }

            // (Opsional tapi direkomendasikan) Tetap dengarkan perubahan login setelah startup
            // Misalnya jika pengguna logout.
            observeActiveUserIdUseCase().drop(1).collect { newUid ->
                if (newUid.isNullOrEmpty()) {
                    _navigationEvent.emit(Auth)
                }
            }
        }

        val appTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        Log.d("LocaleCheck", "AppCompat (per-app) = '$appTag'")

    }

    fun onEvent(event: MainEvent) {
        viewModelScope.launch {
            when (event) {
                is MainEvent.FloatButtonClicked ->
                    _uiState.update { it.copy(isAddActionDialogVisible = true) }

                MainEvent.AddActionDialogDismissed ->
                    _uiState.update { it.copy(isAddActionDialogVisible = false) }

                MainEvent.AddTransactionManuallyClicked ->
                    _navigationEvent.emit(TransactionRoutes.AddEditTransaction())

                MainEvent.ScanReceiptClicked ->
                    _navigationEvent.emit(ScanGraph)
            }
        }
    }
}


