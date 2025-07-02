package com.rifqi.trackfunds.feature.home.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

val DarkCardBackgroundColor = Color(0xFF004780)
val TextOnDarkCardColor = Color.White

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    monthlyBalance: BigDecimal = BigDecimal.ZERO,
    totalExpenses: BigDecimal,
    totalIncome: BigDecimal,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Monthly Balance",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Arrow Forward",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(
                        20.dp
                    )
                )
            }
            Text(
                text = formatCurrency(monthlyBalance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
            ) {
                BalanceDetailItem(
                    label = "Expenses",
                    amount = totalExpenses,
                    iconRes = R.drawable.expense,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.padding(horizontal = 24.dp))
                BalanceDetailItem(
                    label = "Income",
                    amount = totalIncome,
                    iconRes = R.drawable.income,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun BalanceDetailItem(
    label: String,
    amount: BigDecimal,
    @DrawableRes iconRes: Int,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.8f)
            )
            Text(
                formatCurrency(amount),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}


// --- PREVIEWS ---

@Preview(name = "BalanceCard - Loaded State")
@Preview(name = "BalanceCard - Loaded State (Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BalanceCardLoadedPreview() {
    TrackFundsTheme {
        BalanceCard(
            monthlyBalance = BigDecimal("100000"),
            totalExpenses = BigDecimal("100000"),
            totalIncome = BigDecimal("100000"),
            onClick = { },
        )
    }
}

@Preview(name = "BalanceDetailItem - Expense")
@Composable
private fun BalanceDetailItemExpensePreview() {
    TrackFundsTheme {
        // Kita beri Surface dengan warna gelap untuk mensimulasikan latar belakang Card
        Surface(color = DarkCardBackgroundColor) {
            BalanceDetailItem(
                label = "Expenses",
                amount = BigDecimal("1250000.0"),
                iconRes = R.drawable.transfer,
                textColor = TextOnDarkCardColor,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "BalanceDetailItem - Income")
@Composable
private fun BalanceDetailItemIncomePreview() {
    TrackFundsTheme {
        Surface(color = DarkCardBackgroundColor) {
            BalanceDetailItem(
                label = "Income",
                amount = BigDecimal("15000000.0"),
                iconRes = R.drawable.transfer,
                textColor = TextOnDarkCardColor,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}