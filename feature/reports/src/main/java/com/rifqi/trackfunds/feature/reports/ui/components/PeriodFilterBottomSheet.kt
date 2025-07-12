package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.common.model.DateRangeOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PeriodFilterBottomSheet(
    selectedOption: DateRangeOption,
    customStartDate: LocalDate?,
    customEndDate: LocalDate?,
    onOptionClick: (DateRangeOption) -> Unit,
    onCustomDateClick: () -> Unit
) {
    val dateOptions = DateRangeOption.entries.filter { it != DateRangeOption.CUSTOM }

    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
        items(dateOptions) { option ->
            val isSelected = option == selectedOption
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionClick(option) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option.displayName,
                    modifier = Modifier.weight(1f),
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        val isCustomSelected = selectedOption == DateRangeOption.CUSTOM
        val customDateLabel = if (customStartDate != null && customEndDate != null) {
            // Format tanggal jika ada
            val formatter = DateTimeFormatter.ofPattern("d MMM")
            "${customStartDate.format(formatter)} - ${customEndDate.format(formatter)}"
        } else {
            "Custom Date Range"
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCustomDateClick() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = customDateLabel, // Gunakan label dinamis
                    modifier = Modifier.weight(1f),
                    color = if (isCustomSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                    fontWeight = if (isCustomSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (isCustomSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}