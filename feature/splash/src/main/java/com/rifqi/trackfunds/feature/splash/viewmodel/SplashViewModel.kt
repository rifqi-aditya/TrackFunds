package com.rifqi.trackfunds.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.feature.splash.state.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        viewModelScope.launch {
            // .first() mengambil nilai pertama dari Flow, lalu berhenti mengamati.
            // Sempurna untuk pengecekan sekali jalan saat startup.
            val uid = userPreferencesRepository.userUid.first()

            _uiState.update {
                it.copy(
                    startDestination = if (uid != null) HomeGraph else Auth
                )
            }
        }
    }
}