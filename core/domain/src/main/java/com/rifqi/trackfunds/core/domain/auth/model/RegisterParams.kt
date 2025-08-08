package com.rifqi.trackfunds.core.domain.auth.model

data class RegisterParams(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)