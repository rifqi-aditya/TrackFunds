package com.rifqi.trackfunds.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.rifqi.trackfunds.feature.profile.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // TODO: Inject Use Cases di sini
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // Logika untuk memuat data profil pengguna
        _uiState.update { it.copy(isLoading = false) }
    }

    fun onLogoutClicked() {
        // TODO: Implementasi logika logout
        println("Logout clicked")
    }

    // Fungsi-fungsi event handler lainnya bisa ditambahkan di sini
}