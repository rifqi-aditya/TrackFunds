package com.rifqi.trackfunds.feature.home.ui.profile.edit

import android.net.Uri
import java.time.LocalDate

data class EditProfileUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    // Form fields
    val fullName: String = "",
    val username: String = "", // Read-only
    val phoneNumber: String = "",
    val birthdate: LocalDate? = null,
    // Photo
    val photoUrl: String? = null,   // Foto lama dari server
    val newImageUri: Uri? = null, // Foto baru yang dipilih pengguna
    // Validation
    val fullNameError: String? = null,
    // UI control
    val showDatePicker: Boolean = false
)