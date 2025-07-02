package com.rifqi.trackfunds.feature.budget.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun BudgetCardItem(
    item: BudgetItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientBrush = when {
        item.progress > 0.8f -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFF44336), Color(0xFFD32F2F))
        )

        item.progress > 0.6f -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFC107), Color(0xFFFFA000))
        )

        else -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF4AB8B3), Color(0xFF358AC8))
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(
            enabled = true
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DisplayIconFromResource(
                    identifier = item.categoryIconIdentifier,
                    contentDescription = item.categoryName,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.categoryName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Monthly • ${item.period}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Edit Budget",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider()
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian Anggaran dan Sisa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AmountDisplay(label = "Budgeted", amount = item.budgetAmount ?: BigDecimal.ZERO)
                AmountDisplay(
                    label = "Remaining",
                    amount = item.remainingAmount,
                    isRemaining = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GradientHorizontalProgressBar(
                progress = item.progress,
                brush = gradientBrush
            )
        }
    }
}

// Komponen helper kecil untuk menampilkan label dan jumlah
@Composable
private fun RowScope.AmountDisplay(
    label: String,
    amount: BigDecimal,
    isRemaining: Boolean = false
) {
    val amountColor = if (isRemaining && amount < BigDecimal.ZERO) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewBudgetNormal = BudgetItem(
    budgetId = "1", categoryId = "c1", categoryName = "Food & Drink",
    categoryIconIdentifier = "restaurant",
    budgetAmount = BigDecimal("1000000"),
    spentAmount = BigDecimal("750000"),
    period = "June 2025"
)

private val previewBudgetOverspent = BudgetItem(
    budgetId = "2", categoryId = "c2", categoryName = "Transportation",
    categoryIconIdentifier = "commute",
    budgetAmount = BigDecimal("400000"),
    spentAmount = BigDecimal("450000"),
    period = "June 2025"
)

// --- FUNGSI PREVIEW ---
@Preview(name = "Budget Card - Normal", showBackground = true)
@Preview(
    name = "Budget Card - Normal (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BudgetCardItemNormalPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BudgetCardItem(item = previewBudgetNormal, onClick = {})
        }
    }
}

@Preview(name = "Budget Card - Overspent", showBackground = true)
@Composable
private fun BudgetCardItemOverspentPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BudgetCardItem(item = previewBudgetOverspent, onClick = {})
        }
    }
}