package com.rifqi.account.ui.state

data class AddEditAccountUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isAccountSaved: Boolean = false,
    val isEditMode: Boolean = false,
    val screenTitle: String = "Add Account",
    val name: String = "",
    val nameError: String? = null,
    val initialBalance: String = "",
    val iconIdentifier: String = "default_account",
    val showDeleteConfirmDialog: Boolean = false,
    val error: String? = null,
    val iconError: String? = null,
    val showIconPicker: Boolean = false,

)