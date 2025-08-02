package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.common.model.CategorySpending
import com.rifqi.trackfunds.core.domain.category.model.TransactionType

@Composable
fun FinancialAllocationCard(
    activeBreakdownType: TransactionType,
    breakdownData: List<CategorySpending>,
    onBreakdownTypeSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "How is your funds allocated?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Refactor FilterChip untuk menghindari duplikasi
                TransactionType.entries.forEach { type ->
                    if (type == TransactionType.SAVINGS) return@forEach
                    FilterChip(
                        selected = activeBreakdownType == type,
                        onClick = { onBreakdownTypeSelected(type) },
                        label = { Text(type.name) },
                        shape = MaterialTheme.shapes.large,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            selectedLabelColor = MaterialTheme.colorScheme.surface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            selected = activeBreakdownType == type,
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
            Spacer(modifier = Modifier.height(16.dp))
            if (breakdownData.isNotEmpty()) {
                CustomDonutChart(data = breakdownData)
            } else {
                Text("Tidak ada data untuk periode ini.") // Ganti dengan stringResource
            }
        }
    }
}