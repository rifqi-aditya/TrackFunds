package com.rifqi.trackfunds.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.common.model.ValidationResult
import com.rifqi.trackfunds.core.domain.auth.usecase.LoginUserUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.RegisterUserUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateConfirmPassword
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateEmail
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidateFullName
import com.rifqi.trackfunds.core.domain.auth.usecase.validators.ValidatePassword
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
    // Input validators
    private val validateEmail: ValidateEmail,
    private val validateFullName: ValidateFullName,
    private val validatePassword: ValidatePassword,
    private val validateConfirmPassword: ValidateConfirmPassword,
    // Use cases
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.FullNameChanged -> _uiState.update { it.copy(fullName = event.fullName) }
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
                        password = "",
                        confirmPassword = ""
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

        // Panggil setiap validator
        val emailResult = validateEmail(currentState.email)
        val passwordResult = validatePassword(currentState.password)

        // Validasi spesifik untuk mode register
        val fullNameResult =
            if (currentState.authMode == AuthMode.REGISTER) validateFullName(currentState.fullName) else ValidationResult(
                isSuccess = true
            )
        val confirmPasswordResult =
            if (currentState.authMode == AuthMode.REGISTER) validateConfirmPassword(
                currentState.password,
                currentState.confirmPassword
            ) else ValidationResult(isSuccess = true)

        // Kumpulkan semua hasil
        val results = listOf(emailResult, passwordResult, fullNameResult, confirmPasswordResult)
        val hasError = results.any { !it.isSuccess }

        // Perbarui state UI dengan semua error sekaligus
        _uiState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                fullNameError = fullNameResult.errorMessage,
                confirmPasswordError = confirmPasswordResult.errorMessage
            )
        }

        if (hasError) {
            return
        }

        // Jika semua validasi lolos, jalankan aksi utama
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val submissionResult = when (currentState.authMode) {
                AuthMode.LOGIN -> loginUserUseCase(currentState.email, currentState.password)
                AuthMode.REGISTER -> registerUserUseCase(
                    currentState.email,
                    currentState.password,
                    currentState.fullName
                )
            }

            submissionResult
                .onSuccess { _navigationEvent.emit(HomeGraph) }
                .onFailure { error ->
                    _uiState.update { it.copy(emailError = error.message) }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}