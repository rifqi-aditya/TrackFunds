package com.rifqi.trackfunds.core.domain.model

data class User(
    val uid: String,
    val fullName: String? = null,
    val email: String? = null,
    val username: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val birthdate: Long? = null,
    val gender: String? = null
)