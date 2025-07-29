package com.rifqi.trackfunds.feature.budget.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.budget.ui.model.BudgetCategory
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EnhancedBudgetItemRow(
    budgetCategory: BudgetCategory,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with icon and category name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = budgetCategory.icon,
                    contentDescription = budgetCategory.name,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = budgetCategory.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Progress bar
            val progressColor = when {
                budgetCategory.progress > 1f -> MaterialTheme.colorScheme.error
                budgetCategory.progress > 0.8f -> Color(0xFFFFA726) // Orange
                else -> MaterialTheme.colorScheme.primary
            }

            LinearProgressIndicator(
                progress = { budgetCategory.progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Budget details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spent amount
                Column {
                    Text(
                        text = "Spent",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrencyFloat(budgetCategory.spentAmount),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                // Budget amount
                Column {
                    Text(
                        text = "Budget",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatCurrencyFloat(budgetCategory.budgetedAmount),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Remaining amount
            val remainingColor = if (budgetCategory.remainingAmount >= 0f) {
                Color(0xFF4CAF50) // Green for positive
            } else {
                MaterialTheme.colorScheme.error // Red for negative
            }

            Text(
                text = "Sisa ${formatCurrencyFloat(budgetCategory.remainingAmount)} dari ${formatCurrencyFloat(budgetCategory.budgetedAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                color = remainingColor,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

private fun formatCurrencyFloat(amount: Float): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount.toDouble())
}

@Preview(showBackground = true)
@Composable
private fun EnhancedBudgetItemRowPreview() {
    TrackFundsTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Normal budget
            EnhancedBudgetItemRow(
                budgetCategory = BudgetCategory(
                    name = "Makanan & Minuman",
                    icon = Icons.Default.Fastfood,
                    budgetedAmount = 2000000f,
                    spentAmount = 1500000f
                )
            )

            // Near limit budget
            EnhancedBudgetItemRow(
                budgetCategory = BudgetCategory(
                    name = "Transportasi",
                    icon = Icons.Default.LocalGasStation,
                    budgetedAmount = 800000f,
                    spentAmount = 750000f
                )
            )

            // Over budget
            EnhancedBudgetItemRow(
                budgetCategory = BudgetCategory(
                    name = "Shopping",
                    icon = Icons.Default.ShoppingCart,
                    budgetedAmount = 1000000f,
                    spentAmount = 1200000f
                )
            )
        }
    }
}