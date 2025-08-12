package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSheet(
    selected: DateRangeOption,
    customStart: LocalDate?,
    customEnd: LocalDate?,
    onPick: (DateRangeOption) -> Unit,
    onClose: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("d MMM yyyy") }

    SingleChoiceSheet(
        title = "Select Period",
        options = DateRangeOption.entries,
        selected = selected,
        onOptionSelected = onPick,
        onClose = onClose,
        key = { it.name },
        primaryLabel = { it.displayName },
        secondaryLabel = { opt ->
            if (opt == DateRangeOption.CUSTOM && customStart != null && customEnd != null) {
                "${customStart.format(formatter)} – ${customEnd.format(formatter)}"
            } else null
        }
    )
}