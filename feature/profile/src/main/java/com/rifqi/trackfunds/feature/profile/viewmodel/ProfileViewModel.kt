package com.rifqi.trackfunds.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.auth.usecase.LogoutUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.feature.profile.event.ProfileEvent
import com.rifqi.trackfunds.feature.profile.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadUserProfile()
    }


    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.LogoutClicked -> {
                    logoutUseCase()
                    _navigationEvent.emit(Auth)
                }

                ProfileEvent.ManageAccountsClicked -> _navigationEvent.emit(AccountRoutes.Accounts)
                ProfileEvent.ManageCategoriesClicked -> _navigationEvent.emit(SharedRoutes.Categories)
                is ProfileEvent.DarkModeToggled -> {
                    // TODO: Simpan preferensi dark mode
                }
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            // 1. Ambil UID dari DataStore
            userPreferencesRepository.userUid
                .filterNotNull() // Hanya lanjut jika UID ada (pengguna sudah login)
                .flatMapLatest { uid ->
                    // 2. Gunakan UID untuk memanggil Use Case
                    getUserUseCase()
                }
                .collect { userProfile ->
                    if (userProfile != null) {
                        // 3. Update UI State dengan data profil
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                fullName = userProfile.fullName ?: "Anonymous User",
                                email = userProfile.fullName
                                    ?: "-", // Email biasanya didapat dari FirebaseAuth
                                photoUrl = userProfile.photoUrl
                            )
                        }
                    } else {
                        // Handle kasus jika profil tidak ditemukan di Firestore
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                fullName = "User Profile Not Found"
                            )
                        }
                    }
                }
        }
    }
}