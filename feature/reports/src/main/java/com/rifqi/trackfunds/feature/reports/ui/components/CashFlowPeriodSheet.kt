package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashFlowPeriodSheet(
    selected: CashFlowPeriodOption,
    onPick: (CashFlowPeriodOption) -> Unit,
    onClose: () -> Unit
) {
    SingleChoiceSheet(
        title = "Select Cash Flow Period",
        options = CashFlowPeriodOption.entries,
        selected = selected,
        onOptionSelected = onPick,
        onClose = onClose,
        key = { it.name },
        primaryLabel = { it.displayName }
    )
}