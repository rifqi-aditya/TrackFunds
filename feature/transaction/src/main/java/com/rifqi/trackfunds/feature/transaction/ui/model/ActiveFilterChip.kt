package com.rifqi.trackfunds.feature.transaction.ui.model

enum class FilterChipType {
    DATE_RANGE,
    ACCOUNT,
    CATEGORY,
}

data class ActiveFilterChip(
    val id: String,
    val label: String,
    val type: FilterChipType,
)