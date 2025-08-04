package com.rifqi.account.ui.addedit

data class AddEditAccountUiState(
    // Status umum layar
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,

    // Data untuk input form
    val name: String = "",
    val initialBalance: String = "",
    val iconIdentifier: String = "",

    // Status validasi untuk setiap input
    val nameError: String? = null,
    val balanceError: String? = null,
    val iconError: String? = null,

    // Status untuk menampilkan komponen UI
    val showIconPicker: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
)