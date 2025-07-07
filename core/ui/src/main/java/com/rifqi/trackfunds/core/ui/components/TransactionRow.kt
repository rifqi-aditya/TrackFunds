package com.rifqi.trackfunds.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.theme.extendedColors
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime

@Composable
fun TransactionRow(
    item: TransactionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ikon
        DisplayIconFromResource(
            identifier = item.categoryIconIdentifier,
            contentDescription = item.categoryName,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.description.ifBlank { "-" },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (item.type == TransactionType.EXPENSE) {
                    "- ${formatCurrency(item.amount)}"
                } else {
                    formatCurrency(item.amount)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (item.type == TransactionType.EXPENSE) {
                    MaterialTheme.extendedColors.expense
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "4 Jul 25",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            DisplayIconFromResource(
                identifier = item.accountIconIdentifier,
                contentDescription = item.accountName,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
            )
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
        thickness = 1.dp
    )
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewExpenseTransaction = TransactionItem(
    id = "trx1",
    description = "Makan Siang di Kafe",
    amount = BigDecimal("75000"),
    type = TransactionType.EXPENSE,
    date = LocalDateTime.now(),
    categoryId = "cat1",
    categoryName = "Makan & Minum",
    categoryIconIdentifier = "food_and_drink",
    accountId = "acc1",
    accountName = "Dompet Digital",
    accountIconIdentifier = "wallet_account"
)

private val previewIncomeTransaction = TransactionItem(
    id = "trx2",
    description = "",
    amount = BigDecimal("7500000"),
    type = TransactionType.INCOME,
    date = LocalDateTime.now(),
    categoryId = "cat2",
    categoryName = "Gaji",
    categoryIconIdentifier = "salary",
    accountId = "acc2",
    accountName = "Rekening Utama",
    accountIconIdentifier = "bank_account"
)

// --- FUNGSI PREVIEW ---
@Preview(name = "Transaction Row - Light Mode", showBackground = true)
@Preview(
    name = "Transaction Row - Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TransactionRowPreview() {
    TrackFundsTheme {
        // Gunakan Column untuk menumpuk beberapa contoh
        Column {
            TransactionRow(
                item = previewExpenseTransaction,
                onClick = {}
            )
            HorizontalDivider()
            TransactionRow(
                item = previewIncomeTransaction,
                onClick = {}
            )
        }
    }
}