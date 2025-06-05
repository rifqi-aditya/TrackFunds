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
import com.rifqi.trackfunds.feature.home.ui.model.HomeSummary
import com.rifqi.trackfunds.feature.home.util.formatCurrency

// Definisikan warna kustom untuk Card gelap dan teks putih
val DarkCardBackgroundColor = Color(0xFF004780) // Contoh: Abu-abu biru tua yang gelap
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
                .height(190.dp) // Sesuaikan tinggi dengan Card asli
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    DarkCardBackgroundColor.copy(alpha = 0.3f), // Gunakan warna card dengan alpha
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
        shape = MaterialTheme.shapes.large, // Bentuk rounded dari tema
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCardBackgroundColor // Gunakan warna gelap solid untuk Card
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
                modifier = Modifier.fillMaxWidth(),
            ) {
                BalanceDetailItem(
                    label = "Expenses",
                    amount = summary.totalExpenses,
                    // Ganti dengan ID drawable Anda dari R :core:ui
                    iconRes = R.drawable.ic_expense, // Contoh nama drawable
                    textColor = TextOnDarkCardColor // Teks putih
                )
                Spacer(modifier = Modifier.width(32.dp))
                BalanceDetailItem(
                    label = "Income",
                    amount = summary.totalIncome,
                    iconRes = R.drawable.ic_income, // Contoh nama drawable
                    textColor = TextOnDarkCardColor // Teks putih
                )
            }
        }
    }
}

@Composable
fun BalanceDetailItem(
    label: String,
    amount: Double,
    @DrawableRes iconRes: Int, // <-- Tipe parameter diubah ke @DrawableRes Int
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


// --- DATA DUMMY UNTUK PREVIEW (bisa diletakkan di sini atau impor) ---
val previewDummySummaryData = HomeSummary(
    monthlyBalance = 12550750.0,
    totalExpenses = 5325500.0,
    totalIncome = 17875250.0,
    recentExpenses = emptyList(),
    recentIncome = emptyList()
)

// --- PREVIEWS ---

@Preview(showBackground = true, name = "BalanceCard Dark Solid - Light Theme")
@Composable
fun BalanceCardDarkSolidLightPreview() {
    TrackFundsTheme(darkTheme = false) { // Preview di light theme
        BalanceCard(
            summary = previewDummySummaryData,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "BalanceCard Dark Solid - Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BalanceCardDarkSolidDarkPreview() {
    TrackFundsTheme(darkTheme = true) { // Preview di dark theme
        BalanceCard(
            summary = previewDummySummaryData,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "BalanceCard Dark Solid - Loading")
@Composable
fun BalanceCardDarkSolidLoadingPreview() {
    TrackFundsTheme(darkTheme = false) {
        BalanceCard(
            summary = null,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "BalanceDetailItem Expense - On Dark Card")
@Composable
fun BalanceDetailItemExpenseOnDarkPreview() {
    TrackFundsTheme(darkTheme = true) { // Gunakan dark theme untuk simulasi card gelap
        Surface(color = DarkCardBackgroundColor) { // Latar belakang sesuai Card
            BalanceDetailItem(
                label = "Pengeluaran",
                amount = 250000.0,
                iconRes = R.drawable.ic_expense,
                textColor = TextOnDarkCardColor,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "BalanceDetailItem Income - On Dark Card")
@Composable
fun BalanceDetailItemIncomeOnDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface(color = DarkCardBackgroundColor) {
            BalanceDetailItem(
                label = "Pemasukan",
                amount = 1500000.0,
                iconRes = R.drawable.ic_income,
                textColor = TextOnDarkCardColor,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}