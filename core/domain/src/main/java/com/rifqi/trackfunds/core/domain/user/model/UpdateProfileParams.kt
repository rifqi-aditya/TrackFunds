package com.rifqi.trackfunds.core.domain.user.model

import android.net.Uri
import java.time.LocalDate

data class UpdateProfileParams(
    val fullName: String,
    val phoneNumber: String,
    val birthdate: LocalDate?,
    val newImageUri: Uri?
)