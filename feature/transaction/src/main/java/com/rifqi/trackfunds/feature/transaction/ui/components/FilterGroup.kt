package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

data class ChipData(
    val id: String,
    val label: String,
    val iconIdentifier: String? = null,
    val isSelected: Boolean
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterGroup(
    title: String,
    chips: List<ChipData>,
    onChipClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            chips.forEach { chip ->
                FilterChip(
                    selected = chip.isSelected,
                    onClick = { onChipClick(chip.id) },
                    leadingIcon = {
                        if (chip.iconIdentifier != null) {
                            DisplayIconFromResource(
                                identifier = chip.iconIdentifier,
                                contentDescription = chip.label,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(20.dp)
                            )
                        }
                    },
                    label = { Text(chip.label) },
                    shape = MaterialTheme.shapes.large,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        selected = chip.isSelected,
                        enabled = true,

                        selectedBorderColor = MaterialTheme.colorScheme.surface,
                        selectedBorderWidth = 1.5.dp,
                        borderColor = MaterialTheme.colorScheme.outline.copy(
                            alpha = 0.5f
                        ),
                        borderWidth = 1.dp
                    )
                )
            }
        }
    }
}