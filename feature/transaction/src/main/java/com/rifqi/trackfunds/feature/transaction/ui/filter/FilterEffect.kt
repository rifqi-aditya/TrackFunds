package com.rifqi.trackfunds.feature.transaction.ui.filter

sealed interface FilterEffect {
    data object NavigateBack : FilterEffect
    data object OpenDateRangePicker : FilterEffect
}