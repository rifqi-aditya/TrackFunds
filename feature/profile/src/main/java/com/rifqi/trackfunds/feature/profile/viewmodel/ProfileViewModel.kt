package com.rifqi.trackfunds.feature.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.feature.profile.event.ProfileEvent
import com.rifqi.trackfunds.feature.profile.state.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        _uiState.update { it.copy(isLoading = false) }
    }


    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.LogoutClicked -> {}
                ProfileEvent.SettingsClicked -> {}
                ProfileEvent.ManageAccountsClicked -> _navigationEvent.emit(AccountRoutes.Accounts)
                ProfileEvent.ManageCategoriesClicked -> _navigationEvent.emit(SharedRoutes.Categories)
                is ProfileEvent.DarkModeToggled -> {
                    // TODO: Simpan preferensi dark mode
                }
            }
        }
    }
}