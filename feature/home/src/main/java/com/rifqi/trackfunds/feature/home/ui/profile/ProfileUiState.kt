package com.rifqi.trackfunds.feature.home.ui.profile

/**
 * Merepresentasikan seluruh keadaan (state) untuk ProfileScreen.
 */
data class ProfileUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userEmail: String = "",
    val photoUrl: String? = null,
    val phoneNumber: String = "",
    val birthdate: String = "",
    val gender: String = "",
    val error: String? = null
)