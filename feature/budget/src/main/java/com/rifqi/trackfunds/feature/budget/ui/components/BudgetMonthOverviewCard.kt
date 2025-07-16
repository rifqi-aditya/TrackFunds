package com.rifqi.trackfunds.feature.budget.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun BudgetMonthOverviewCard(modifier: Modifier = Modifier, title: String, amount: BigDecimal) {
    Card(
        modifier = Modifier.defaultMinSize(minWidth = 150.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                text = formatCurrency(amount),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BudgetMonthOverviewCardPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BudgetMonthOverviewCard(
                title = "Total Budgeted",
                amount = BigDecimal(6000000)
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun BudgetMonthOverviewCardDetailedPreview() {
    TrackFundsTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BudgetMonthOverviewCard(
                title = "Total Spent",
                amount = BigDecimal(6000000)
            )
        }
    }
}



