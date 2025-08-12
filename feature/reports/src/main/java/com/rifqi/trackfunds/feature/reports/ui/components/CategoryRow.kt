package com.rifqi.trackfunds.feature.reports.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.reports.ui.state.CategorySummaryUiModel

@Composable
fun CategoryRow(summary: CategorySummaryUiModel) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(summary.categoryName, fontWeight = FontWeight.SemiBold)
            Text(formatCurrency(summary.totalAmount), fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = { summary.percentage },
                modifier = Modifier.weight(1f),
                color = summary.color
            )
            Text(
                "${(summary.percentage * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}