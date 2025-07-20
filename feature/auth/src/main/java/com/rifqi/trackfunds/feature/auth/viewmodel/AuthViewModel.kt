package com.rifqi.trackfunds.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.auth.LoginUserUseCase
import com.rifqi.trackfunds.core.domain.usecase.auth.RegisterUserUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.feature.auth.event.AuthEvent
import com.rifqi.trackfunds.feature.auth.state.AuthMode
import com.rifqi.trackfunds.feature.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> _uiState.update { it.copy(email = event.email) }
            is AuthEvent.PasswordChanged -> _uiState.update { it.copy(password = event.password) }
            is AuthEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.confirmPassword) }
            is AuthEvent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            AuthEvent.SwitchMode -> {
                val newMode =
                    if (_uiState.value.authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                _uiState.update {
                    it.copy(
                        authMode = newMode,
                        errorMessage = null,
                        password = "",
                        confirmPassword = ""
                    )
                }
            }

            AuthEvent.Submit -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = when (currentState.authMode) {
                AuthMode.LOGIN -> {
                    loginUserUseCase(currentState.email, currentState.password)
                }

                AuthMode.REGISTER -> {
                    if (currentState.password != currentState.confirmPassword) {
                        Result.failure(Exception("Passwords do not match."))
                    } else {
                        registerUserUseCase(currentState.email, currentState.password)
                    }
                }
            }

            result
                .onSuccess {
                    _navigationEvent.emit(HomeGraph)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}