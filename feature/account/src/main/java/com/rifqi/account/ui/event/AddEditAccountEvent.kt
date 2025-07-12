package com.rifqi.account.ui.event

sealed interface AddEditAccountEvent {
    data class NameChanged(val name: String) : AddEditAccountEvent
    data class BalanceChanged(val balance: String) : AddEditAccountEvent
    data class IconChanged(val iconIdentifier: String) : AddEditAccountEvent
    data object SaveAccountClicked : AddEditAccountEvent
    data object DeleteClicked : AddEditAccountEvent
    data object ConfirmDeleteClicked : AddEditAccountEvent
    data object DismissDeleteDialog : AddEditAccountEvent
    data object ShowIconPickerClicked : AddEditAccountEvent
    data object IconPickerDismissed : AddEditAccountEvent
    data class IconIdentifierChanged(val identifier: String) : AddEditAccountEvent

}