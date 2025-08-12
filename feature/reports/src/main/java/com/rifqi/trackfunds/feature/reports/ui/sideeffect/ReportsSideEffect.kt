package com.rifqi.trackfunds.feature.reports.ui.sideeffect

sealed interface ReportsSideEffect {
    data object ShowDateRangePicker : ReportsSideEffect
}