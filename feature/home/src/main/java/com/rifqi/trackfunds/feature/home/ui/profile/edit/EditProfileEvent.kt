package com.rifqi.trackfunds.feature.home.ui.profile.edit

import android.net.Uri
import java.time.LocalDate

sealed interface EditProfileEvent {
    data class FullNameChanged(val name: String) : EditProfileEvent
    data class PhoneNumberChanged(val phone: String) : EditProfileEvent
    data object ChangePhotoClicked : EditProfileEvent
    data class ImageSelected(val uri: Uri) : EditProfileEvent
    data object BirthdateClicked : EditProfileEvent
    data class DateSelected(val date: LocalDate) : EditProfileEvent
    data object DismissDatePicker : EditProfileEvent
    data object SaveClicked : EditProfileEvent
}