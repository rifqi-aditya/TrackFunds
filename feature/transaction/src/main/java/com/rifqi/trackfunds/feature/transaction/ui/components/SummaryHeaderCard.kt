package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    totalIncome: BigDecimal,
    totalExpense: BigDecimal,
    netBalance: BigDecimal,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SummaryItem(
                label = "Income",
                amount = totalIncome,
                iconIdentifier = "income",
                amountColor = TrackFundsTheme.extendedColors.income
            )
            SummaryItem(
                label = "Expense",
                amount = totalExpense,
                iconIdentifier = "expense",
                amountColor = TrackFundsTheme.extendedColors.expense
            )
            SummaryItem(
                label = "Net",
                amount = netBalance,
                iconIdentifier = "net",
                amountColor = Color(0xFF0D5EA6)
            )
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
            totalIncome = BigDecimal("1000000"),
            totalExpense = BigDecimal("470000"),
            netBalance = BigDecimal("530000"),
        )
    }
}