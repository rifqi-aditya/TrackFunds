package com.rifqi.trackfunds.core.domain.user.model

import java.time.LocalDate

data class User(
    val uid: String,
    val fullName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val birthdate: LocalDate? = null,
    val createdAt: Long
)