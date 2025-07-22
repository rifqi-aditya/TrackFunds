package com.rifqi.trackfunds.feature.profile.state

data class ProfileUiState(
    val isLoading: Boolean = true,
    val fullName: String = "Loading...",
    val email: String = "...",
    val photoUrl: String? = null,
    val appVersion: String = "1.0.0"
)