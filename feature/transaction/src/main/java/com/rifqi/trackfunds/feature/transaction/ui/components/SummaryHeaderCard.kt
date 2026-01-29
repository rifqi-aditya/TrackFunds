package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

// Net balance color
private val NetBalanceColor = Color(0xFF1976D2)

@Composable
fun SummaryCard(
    totalIncome: BigDecimal,
    totalExpense: BigDecimal,
    netBalance: BigDecimal,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem(
                label = "Income",
                amount = totalIncome,
                icon = Icons.Rounded.ArrowDownward,
                iconBackgroundColor = TrackFundsTheme.extendedColors.income.copy(alpha = 0.15f),
                iconTint = TrackFundsTheme.extendedColors.income,
                amountColor = TrackFundsTheme.extendedColors.income
            )
            SummaryItem(
                label = "Expense",
                amount = totalExpense,
                icon = Icons.Rounded.ArrowUpward,
                iconBackgroundColor = TrackFundsTheme.extendedColors.expense.copy(alpha = 0.15f),
                iconTint = TrackFundsTheme.extendedColors.expense,
                amountColor = TrackFundsTheme.extendedColors.expense
            )
            SummaryItem(
                label = "Net",
                amount = netBalance,
                icon = Icons.Rounded.Balance,
                iconBackgroundColor = NetBalanceColor.copy(alpha = 0.15f),
                iconTint = NetBalanceColor,
                amountColor = NetBalanceColor
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    amount: BigDecimal,
    icon: ImageVector,
    iconBackgroundColor: Color,
    iconTint: Color,
    amountColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Icon with circular background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
        
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Amount
        Text(
            text = formatCurrency(amount),
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