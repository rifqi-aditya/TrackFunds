package com.rifqi.trackfunds.feature.home.ui.profile

/**
 * Merepresentasikan perintah sekali jalan dari ViewModel ke UI.
 */
sealed interface ProfileSideEffect {
    data object NavigateToEditProfile : ProfileSideEffect
}