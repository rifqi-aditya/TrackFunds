package com.rifqi.trackfunds.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.auth.model.LoginParams
import com.rifqi.trackfunds.core.domain.auth.model.RegisterParams
import com.rifqi.trackfunds.core.domain.auth.usecase.LoginUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.RegisterUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateConfirmPassword
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateEmail
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateFullName
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidatePassword
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import com.rifqi.trackfunds.feature.auth.event.AuthEvent
import com.rifqi.trackfunds.feature.auth.sideeffect.AuthSideEffect
import com.rifqi.trackfunds.feature.auth.state.AuthMode
import com.rifqi.trackfunds.feature.auth.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validateFullName: ValidateFullName,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<AuthSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.FullNameChanged -> _uiState.update {
                it.copy(
                    fullName = event.fullName,
                    fullNameError = null
                )
            }

            is AuthEvent.EmailChanged -> _uiState.update {
                it.copy(
                    email = event.email,
                    emailError = null
                )
            }

            is AuthEvent.PasswordChanged -> _uiState.update {
                it.copy(
                    password = event.password,
                    passwordError = null
                )
            }

            is AuthEvent.ConfirmPasswordChanged -> _uiState.update {
                it.copy(
                    confirmPassword = event.confirmPassword,
                    confirmPasswordError = null
                )
            }

            is AuthEvent.TogglePasswordVisibility -> _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }

            AuthEvent.SwitchMode -> {
                val newMode =
                    if (_uiState.value.authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                _uiState.update {
                    it.copy(
                        authMode = newMode,
                        password = "",
                        confirmPassword = "",
                        generalError = null,
                    )
                }
            }

            AuthEvent.Submit -> handleSubmit()
            AuthEvent.ForgotPasswordClicked -> {

            }
        }
    }

    private fun handleSubmit() {
        val currentState = _uiState.value

        // Validasi kondisional Anda di sini sudah bagus
        val emailResult = validateEmail(currentState.email)
        val passwordResult = validatePassword(currentState.password)
        val fullNameResult =
            if (currentState.authMode == AuthMode.REGISTER) validateFullName(currentState.fullName) else ValidationResult(
                true
            )
        val confirmPasswordResult =
            if (currentState.authMode == AuthMode.REGISTER) validateConfirmPassword(
                currentState.password,
                currentState.confirmPassword
            ) else ValidationResult(true)

        val hasError = listOf(
            emailResult,
            passwordResult,
            fullNameResult,
            confirmPasswordResult
        ).any { !it.isSuccess }

        _uiState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                fullNameError = fullNameResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage,
                generalError = null
            )
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result: Result<*> = when (currentState.authMode) {
                AuthMode.LOGIN -> {
                    // Gunakan LoginParams
                    val params = LoginParams(currentState.email, currentState.password)
                    loginUseCase(params)
                }

                AuthMode.REGISTER -> {
                    // Gunakan RegisterParams
                    val params = RegisterParams(
                        fullName = currentState.fullName,
                        email = currentState.email,
                        password = currentState.password,
                        confirmPassword = currentState.confirmPassword
                    )
                    registerUseCase(params)
                }
            }

            result.onSuccess {
                _sideEffect.send(AuthSideEffect.NavigateToHome)
            }.onFailure { error ->
                _uiState.update { it.copy(generalError = error.message) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}