package com.rifqi.trackfunds.feature.home.ui.profile

/**
 * Merepresentasikan semua aksi yang bisa dilakukan pengguna di layar.
 */
sealed interface ProfileEvent {
    data object EditProfileClicked : ProfileEvent
}