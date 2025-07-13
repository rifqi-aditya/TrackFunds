package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun SummaryCard(
    spendableBalance: BigDecimal,
    totalIncome: BigDecimal,
    totalExpense: BigDecimal,
    totalSavings: BigDecimal,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your Remaining Balance",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                formatCurrency(spendableBalance),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryItem(
                    label = "Income",
                    amount = totalIncome,
                    iconIdentifier = "income",
                    amountColor = TrackFundsTheme.extendedColors.textIncome
                )
                SummaryItem(
                    label = "Expense",
                    amount = totalExpense,
                    iconIdentifier = "expense",
                    amountColor = TrackFundsTheme.extendedColors.textExpense
                )
                SummaryItem(
                    label = "Savings",
                    amount = totalSavings,
                    iconIdentifier = "savings",
                    amountColor = Color(0xFF0D5EA6)
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    amount: BigDecimal,
    amountColor: Color,
    iconIdentifier: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(
            formatCurrency(amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SummaryCardPreview() {
    TrackFundsTheme {
        SummaryCard(
            spendableBalance = BigDecimal("530000"),
            totalIncome = BigDecimal("1000000"),
            totalExpense = BigDecimal("470000"),
            totalSavings = BigDecimal.ZERO
        )
    }
}