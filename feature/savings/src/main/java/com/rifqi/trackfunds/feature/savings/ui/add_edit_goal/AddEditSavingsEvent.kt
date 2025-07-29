package com.rifqi.trackfunds.feature.savings.ui.add_edit_goal

import java.time.LocalDate

sealed interface AddEditSavingsEvent {
    // Aksi pada Form
    data class NameChanged(val name: String) : AddEditSavingsEvent
    data class TargetAmountChanged(val amount: String) : AddEditSavingsEvent
    data class DateSelected(val date: LocalDate) : AddEditSavingsEvent
    data class IconIdentifierChanged(val identifier: String) : AddEditSavingsEvent

    // Aksi Klik
    data object ShowIconPickerClicked : AddEditSavingsEvent
    data object IconPickerDismissed : AddEditSavingsEvent
    data object DateSelectorClicked : AddEditSavingsEvent
    data object DatePickerDismissed : AddEditSavingsEvent
    data object SaveClicked : AddEditSavingsEvent
}