package com.rifqi.trackfunds.feature.profile.model

// Model data UI State untuk layar Profil (disesuaikan)
data class ProfileUiState(
    val isLoading: Boolean = true,
    val userName: String = "Zen Panda",
    val userStatus: String = "Anonymous", // Mengganti email dengan status
    val appVersion: String = "1.0.0",
    val error: String? = null
)