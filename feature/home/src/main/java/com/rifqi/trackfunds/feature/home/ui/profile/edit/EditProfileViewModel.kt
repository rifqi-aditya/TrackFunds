package com.rifqi.trackfunds.feature.home.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.user.model.UpdateProfileParams
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.UpdateUserUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.ValidateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val validateProfileUseCase: ValidateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<EditProfileSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getUserUseCase().first()?.let { user ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        fullName = user.fullName ?: "",
                        phoneNumber = user.phoneNumber ?: "",
                        birthdate = user.birthdate,
                        photoUrl = user.photoUrl
                    )
                }
            } ?: _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.FullNameChanged -> _uiState.update {
                it.copy(
                    fullName = event.name,
                    fullNameError = null
                )
            }

            is EditProfileEvent.PhoneNumberChanged -> _uiState.update { it.copy(phoneNumber = event.phone) }
            EditProfileEvent.ChangePhotoClicked -> sendSideEffect(EditProfileSideEffect.LaunchImagePicker)
            is EditProfileEvent.ImageSelected -> _uiState.update { it.copy(newImageUri = event.uri) }
            EditProfileEvent.BirthdateClicked -> _uiState.update { it.copy(showDatePicker = true) }
            is EditProfileEvent.DateSelected -> _uiState.update {
                it.copy(
                    birthdate = event.date,
                    showDatePicker = false
                )
            }

            EditProfileEvent.DismissDatePicker -> _uiState.update { it.copy(showDatePicker = false) }
            EditProfileEvent.SaveClicked -> saveProfile()
        }
    }

    private fun saveProfile() {
        val currentState = _uiState.value

        val validationResult = validateProfileUseCase(currentState.fullName)
        if (!validationResult.isSuccess) {
            _uiState.update { it.copy(fullNameError = validationResult.fullNameError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val params = UpdateProfileParams(
                fullName = currentState.fullName,
                phoneNumber = currentState.phoneNumber,
                birthdate = currentState.birthdate,
                newImageUri = currentState.newImageUri
            )

            updateUserUseCase(params)
                .onSuccess {
                    sendSideEffect(EditProfileSideEffect.ShowSnackbar("Profile updated successfully"))
                    sendSideEffect(EditProfileSideEffect.NavigateBack)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSaving = false) }
                    sendSideEffect(
                        EditProfileSideEffect.ShowSnackbar(
                            error.message ?: "Update failed"
                        )
                    )
                }
        }
    }

    private fun sendSideEffect(effect: EditProfileSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}