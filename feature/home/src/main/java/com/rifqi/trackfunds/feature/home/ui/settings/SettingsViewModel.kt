package com.rifqi.trackfunds.feature.home.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.auth.usecase.LogoutUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveAppThemeUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveLocaleUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetAppThemeUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetLocaleUseCase
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

    observeAppThemeUseCase: ObserveAppThemeUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
    observeLocaleUseCase: ObserveLocaleUseCase,
    private val setLocaleUseCase: SetLocaleUseCase,

    private val getAppVersionUseCase: GetAppVersionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SettingsSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        observeInitialData(observeAppThemeUseCase, observeLocaleUseCase)
    }

    private fun observeInitialData(
        observeAppTheme: ObserveAppThemeUseCase,
        observeLocale: ObserveLocaleUseCase
    ) {
        combine(
            getUserUseCase(),
            observeAppTheme(),
            observeLocale()
        ) { user, theme, locale ->
            Triple(user, theme, locale)
        }
            .onEach { (user, theme, locale) ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        userName = user?.fullName ?: "Guest",
                        userEmail = user?.email ?: "",
                        userPhotoUrl = user?.photoUrl,
                        appTheme = theme,
                        localeTag = locale,
                        appVersion = getAppVersionUseCase()
                    )
                }
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

                SettingsEvent.ThemeItemClicked ->
                    _uiState.update { it.copy(showThemePickerDialog = true) }

                is SettingsEvent.ThemeChanged -> {
                    setAppThemeUseCase(event.theme)
                    _uiState.update { it.copy(showThemePickerDialog = false) }
                }

                SettingsEvent.DismissThemeDialog ->
                    _uiState.update { it.copy(showThemePickerDialog = false) }

                SettingsEvent.LanguageItemClicked ->
                    _uiState.update { it.copy(showLanguagePickerDialog = true) }

                SettingsEvent.DismissLanguageDialog ->
                    _uiState.update { it.copy(showLanguagePickerDialog = false) }

                is SettingsEvent.LanguageChanged -> {
                    setLocaleUseCase(event.tag)
                    _uiState.update { it.copy(showLanguagePickerDialog = false) }
                    _sideEffect.send(SettingsSideEffect.ApplyLocale(event.tag))
                }

                SettingsEvent.LogoutClicked ->
                    _uiState.update { it.copy(showLogoutConfirmDialog = true) }

                SettingsEvent.DismissLogoutDialog ->
                    _uiState.update { it.copy(showLogoutConfirmDialog = false) }

                SettingsEvent.ConfirmLogoutClicked -> {
                    _uiState.update { it.copy(showLogoutConfirmDialog = false, isLoading = true) }
                    logoutUseCase()
                        .onSuccess { _sideEffect.send(SettingsSideEffect.NavigateToLogin) }
                        .onFailure { _uiState.update { it.copy(isLoading = false) } }
                }
            }
        }
    }
}
