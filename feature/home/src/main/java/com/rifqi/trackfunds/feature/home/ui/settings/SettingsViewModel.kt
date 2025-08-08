package com.rifqi.trackfunds.feature.home.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.auth.usecase.LogoutUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetThemePreferenceUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetThemePreferenceUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase,
    private val setThemePreferenceUseCase: SetThemePreferenceUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SettingsSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeInitialData()
    }

    private fun observeInitialData() {
        // Gabungkan flow dari repository di sini
        combine(
            getUserUseCase(),
            getThemePreferenceUseCase()
        ) { user, theme ->
            // Siapkan data yang akan di-update
            SettingsUiState(
                isLoading = false,
                userName = user?.fullName ?: "Guest",
                userEmail = user?.email ?: "",
                userPhotoUrl = user?.photoUrl,
                appTheme = theme,
                appVersion = getAppVersionUseCase(),
                // Pertahankan state dialog dari nilai sebelumnya
                showLogoutConfirmDialog = _uiState.value.showLogoutConfirmDialog
            )
        }.onEach { newState ->
            // Update state dengan data baru dari flow
            _uiState.value = newState
        }
            .launchIn(viewModelScope)
    }


    fun onEvent(event: SettingsEvent) {
        viewModelScope.launch {
            when (event) {
                SettingsEvent.ProfileClicked -> _sideEffect.send(SettingsSideEffect.NavigateToProfile)
                SettingsEvent.ManageAccountsClicked -> _sideEffect.send(SettingsSideEffect.NavigateToManageAccounts)
                SettingsEvent.ManageCategoriesClicked -> _sideEffect.send(SettingsSideEffect.NavigateToManageCategories)
                SettingsEvent.SecurityClicked -> _sideEffect.send(SettingsSideEffect.NavigateToSecurity)
                SettingsEvent.ThemeItemClicked -> {
                    _uiState.update { it.copy(showThemePickerDialog = true) }
                }

                is SettingsEvent.ThemeChanged -> {
                    setThemePreferenceUseCase(event.theme)
                    _uiState.update { it.copy(showThemePickerDialog = false) }
                }

                SettingsEvent.LogoutClicked -> _uiState.update { it.copy(showLogoutConfirmDialog = true) }
                SettingsEvent.DismissLogoutDialog -> _uiState.update {
                    it.copy(
                        showLogoutConfirmDialog = false
                    )
                }

                SettingsEvent.ConfirmLogoutClicked -> {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                showLogoutConfirmDialog = false,
                                isLoading = true
                            )
                        }
                        logoutUseCase()
                            .onSuccess {
                                _sideEffect.send(SettingsSideEffect.NavigateToLogin)
                            }
                            .onFailure {
                                _uiState.update { it.copy(isLoading = false) }
                            }
                    }
                }

                SettingsEvent.DismissThemeDialog -> {
                    _uiState.update { it.copy(showThemePickerDialog = false) }
                }
            }
        }
    }
}