package com.rifqi.trackfunds.feature.home.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.rifqi.trackfunds.core.ui.util.formatCurrency
import com.rifqi.trackfunds.feature.home.ui.model.HomeSummary
import java.math.BigDecimal

val DarkCardBackgroundColor = Color(0xFF004780)
val TextOnDarkCardColor = Color.White

@Composable
fun BalanceCard(
    summary: HomeSummary?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (summary == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    DarkCardBackgroundColor.copy(alpha = 0.3f),
                    shape = MaterialTheme.shapes.large
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp), color = TextOnDarkCardColor)
        }
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCardBackgroundColor
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
                    color = TextOnDarkCardColor,
                )
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward_ios),
                    contentDescription = "Lihat Detail",
                    tint = TextOnDarkCardColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = formatCurrency(summary.monthlyBalance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextOnDarkCardColor,
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
                    amount = summary.totalExpenses,
                    iconRes = R.drawable.ic_expense,
                    textColor = TextOnDarkCardColor
                )
                Spacer(modifier = Modifier.padding(horizontal = 24.dp))
                BalanceDetailItem(
                    label = "Income",
                    amount = summary.totalIncome,
                    iconRes = R.drawable.ic_income,
                    textColor = TextOnDarkCardColor
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

private val previewDummySummaryData = HomeSummary(
    monthlyBalance = BigDecimal("13750000"),
    totalExpenses = BigDecimal("1250000"),
    totalIncome = BigDecimal("15000000"),
    // List ini bisa kosong karena tidak ditampilkan langsung oleh BalanceCard
    expenseSummaries = emptyList(),
    incomeSummaries = emptyList()
)


// --- PREVIEWS ---

@Preview(name = "BalanceCard - Loaded State")
@Preview(name = "BalanceCard - Loaded State (Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BalanceCardLoadedPreview() {
    TrackFundsTheme {
        BalanceCard(
            summary = previewDummySummaryData,
            onClick = {}
        )
    }
}

@Preview(name = "BalanceCard - Loading State")
@Composable
private fun BalanceCardLoadingPreview() {
    TrackFundsTheme {
        // Berikan null pada summary untuk melihat tampilan loading
        BalanceCard(
            summary = null,
            onClick = {}
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
                iconRes = R.drawable.ic_expense,
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
                iconRes = R.drawable.ic_income,
                textColor = TextOnDarkCardColor,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}