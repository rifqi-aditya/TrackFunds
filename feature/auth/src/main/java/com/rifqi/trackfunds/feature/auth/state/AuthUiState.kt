package com.rifqi.trackfunds.feature.auth.state

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val authMode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false,

    // Form fields and their errors
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isPasswordVisible: Boolean = false,
)