package com.rifqi.trackfunds.feature.auth.event

sealed interface AuthEvent {
    // Aksi saat input teks berubah
    data class FullNameChanged(val fullName: String) : AuthEvent
    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent

    // Aksi saat tombol utama ditekan (bisa untuk login atau register)
    data object Submit : AuthEvent

    // Aksi untuk beralih antara mode login dan register
    data object SwitchMode : AuthEvent

    // Aksi untuk menampilkan/menyembunyikan password
    data object TogglePasswordVisibility : AuthEvent

    data object ForgotPasswordClicked : AuthEvent
}