package com.rifqi.trackfunds.feature.home.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import com.rifqi.trackfunds.core.ui.utils.formatLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = getUserUseCase()
        .map { user ->
            // 2. ViewModel melakukan mapping dari domain ke UiState
            ProfileUiState(
                isLoading = false,
                userName = user?.fullName ?: "Guest User",
                userEmail = user?.email ?: "No email",
                photoUrl = user?.photoUrl,
                phoneNumber = user?.phoneNumber ?: "",
                birthdate = user?.birthdate?.let(::formatLocalDate) ?: "",
            )
        }
        .catch { e -> emit(ProfileUiState(isLoading = false, error = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    private val _sideEffect = Channel<ProfileSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.EditProfileClicked -> {
                viewModelScope.launch {
                    _sideEffect.send(ProfileSideEffect.NavigateToEditProfile)
                }
            }
        }
    }
}