package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.rifqi.trackfunds.core.ui.components.GradientHorizontalProgressBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.YearMonth

@Composable
fun BudgetSummaryRow(
    modifier: Modifier = Modifier,
    item: BudgetItem,
) {
    val gradientBrush = when {
        item.progress > 0.8f -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFF44336), Color(0xFFD32F2F))
        )

        item.progress > 0.6f -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFFFC107), Color(0xFFFFA000))
        )

        else -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF46911E), Color(0xFF46911E))
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.categoryName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${formatCurrency(item.spentAmount)} / ${formatCurrency(item.budgetAmount)}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        GradientHorizontalProgressBar(
            progress = item.progress,
            brush = gradientBrush
        )
    }
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewBudgetSafe = BudgetItem(
    budgetId = "1", categoryId = "c1", categoryName = "Belanja",
    categoryIconIdentifier = "shopping", budgetAmount = BigDecimal("2000000"),
    spentAmount = BigDecimal("400000"), period = YearMonth.now()
)

// --- FUNGSI PREVIEW ---
@Preview(showBackground = true, name = "Budget Summary Row States")
@Composable
private fun BudgetSummaryRowPreview() {
    TrackFundsTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BudgetSummaryRow(item = previewBudgetSafe)
        }
    }
}