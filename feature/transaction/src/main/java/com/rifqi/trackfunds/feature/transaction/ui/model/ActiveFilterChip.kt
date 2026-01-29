package com.rifqi.trackfunds.feature.transaction.ui.model

import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption

enum class FilterChipType {
    DATE_RANGE,
    ACCOUNT,
    CATEGORY,
}

data class ActiveFilterChip(
    val id: String,
    val label: String,
    val type: FilterChipType,
    val dateOption: DateRangeOption? = null,
)