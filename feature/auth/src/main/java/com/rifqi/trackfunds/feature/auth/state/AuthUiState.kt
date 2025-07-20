package com.rifqi.trackfunds.feature.auth.state

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val authMode: AuthMode = AuthMode.LOGIN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordVisible: Boolean = false
)