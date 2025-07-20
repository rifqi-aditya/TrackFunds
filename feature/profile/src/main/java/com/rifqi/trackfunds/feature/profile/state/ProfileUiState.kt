package com.rifqi.trackfunds.feature.profile.state

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "...",
    val email: String = "...",
    val userStatus: String = "...",
    val appVersion: String = "..."
)