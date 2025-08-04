package com.rifqi.account.ui.addedit

sealed interface AddEditAccountSideEffect {
    data object NavigateBack : AddEditAccountSideEffect
    data class ShowSnackbar(val message: String) : AddEditAccountSideEffect
}