package com.rifqi.trackfunds.core.common.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sebuah manager terpusat untuk menampilkan Snackbar di seluruh aplikasi.
 * Dibuat sebagai Singleton agar hanya ada satu instance.
 */
@Singleton
class SnackbarManager @Inject constructor() {
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}