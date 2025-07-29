package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun MonthFilterChips(
    selectedMonth: String,
    availableMonths: List<String>,
    onMonthSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(availableMonths) { month ->
            FilterChip(
                onClick = { onMonthSelected(month) },
                label = {
                    Text(
                        text = month,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                selected = month == selectedMonth,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = month == selectedMonth,
                    borderColor = if (month == selectedMonth) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthFilterChipsPreview() {
    TrackFundsTheme {
        MonthFilterChips(
            selectedMonth = "Juli 2024",
            availableMonths = listOf("Juni 2024", "Juli 2024", "Agustus 2024", "September 2024"),
            onMonthSelected = {},
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}