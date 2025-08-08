package com.rifqi.trackfunds.feature.home.ui.profile.edit

sealed interface EditProfileSideEffect {
    data object NavigateBack : EditProfileSideEffect
    data object LaunchImagePicker : EditProfileSideEffect
    data class ShowSnackbar(val message: String) : EditProfileSideEffect
}