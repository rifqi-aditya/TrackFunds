package com.rifqi.trackfunds.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.util.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.util.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun formatTransactionDate(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy hh:mm a", Locale.getDefault())
    return dateTime.format(formatter)
}

@Composable
fun TransactionListItem(
    transaction: TransactionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayIconFromResource(
                identifier = transaction.iconIdentifier,
                contentDescription = transaction.categoryName,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.note.ifEmpty { transaction.categoryName },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formatTransactionDate(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // === PERUBAHAN DI SINI ===
            val isExpense = transaction.type == TransactionType.EXPENSE

            // Tentukan warna teks dan background berdasarkan tipe transaksi
            val amountColor = if (isExpense) {
                MaterialTheme.colorScheme.onSurface // Warna teks utama untuk expense
            } else {
                MaterialTheme.colorScheme.tertiary // Warna hijau pekat untuk income
            }

            val containerColor = if (isExpense) {
                Color.Transparent // Tidak ada background untuk expense
            } else {
                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f) // Background hijau pastel untuk income
            }

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small) // Bentuk rounded untuk chip
                    .background(containerColor) // Terapkan warna background kondisional
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = formatCurrency(transaction.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor // Terapkan warna teks kondisional
                )
            }
        }
        HorizontalDivider()
    }
}

// --- Preview untuk Komponen ---
@Preview(showBackground = true, name = "TransactionListItem - Expense")
@Composable
private fun TransactionListItemExpensePreview() {
    TrackFundsTheme {
        val expenseItem = TransactionItem(
            id = "1",
            note = "Makan Malam di Luar",
            amount = BigDecimal("125000"),
            type = TransactionType.EXPENSE,
            date = LocalDateTime.now(),
            categoryId = "cat1",
            categoryName = "Makanan",
            iconIdentifier = "restaurant",
            accountId = "acc1",
            accountName = "acc1"
        )
        TransactionListItem(transaction = expenseItem, onClick = {})
    }
}

@Preview(
    showBackground = true,
    name = "TransactionListItem - Income",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TransactionListItemIncomePreview() {
    TrackFundsTheme(darkTheme = true) {
        val incomeItem = TransactionItem(
            id = "2",
            note = "Gaji Bulan Ini",
            amount = BigDecimal("125000"),
            type = TransactionType.INCOME,
            date = LocalDateTime.now().minusDays(5),
            categoryId = "cat2",
            categoryName = "Gaji",
            iconIdentifier = "cash",
            accountId = "acc2",
            accountName = "acc2"
        )
        TransactionListItem(transaction = incomeItem, onClick = {})
    }
}