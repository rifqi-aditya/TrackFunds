package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.ui.util.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.util.formatCurrency

@Composable
fun BudgetItemRow(
    item: BudgetItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Bagian Header (Ikon dan Nama Kategori)
            Row(verticalAlignment = Alignment.CenterVertically) {
                DisplayIconFromResource(
                    identifier = item.categoryIconIdentifier,
                    contentDescription = item.categoryName,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Teks Progres (Rp terpakai / Rp dianggarkan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatCurrency(item.spentAmount),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "/ ${formatCurrency(item.budgetAmount ?: item.spentAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Progress Bar Visual
            val progressColor = when {
                item.progress > 1f -> MaterialTheme.colorScheme.error // Melebihi budget
                item.progress > 0.8f -> Color(0xFFFFA726) // Orange, mendekati limit
                else -> MaterialTheme.colorScheme.primary
            }
            LinearProgressIndicator(
                progress = { item.progress.coerceAtMost(1f) }, // progress tidak lebih dari 100%
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                color = progressColor
            )

            // Teks Sisa Budget
            Text(
                text = "Remaining: ${formatCurrency(item.remainingAmount)}",
                style = MaterialTheme.typography.bodySmall,
                color = if (item.remainingAmount < java.math.BigDecimal.ZERO) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}