package com.rifqi.trackfunds.feature.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RecentTransactionRow(
    modifier: Modifier = Modifier,
    item: TransactionItem,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayIconFromResource(
            identifier = item.categoryIconIdentifier,
            contentDescription = item.categoryName,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f))
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.description.ifEmpty { "-" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.padding(top = 4.dp, end = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DisplayIconFromResource(
                    identifier = item.accountIconIdentifier,
                    contentDescription = item.accountName,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f))
                        .padding(4.dp)
                )
                Text(
                    text = item.accountName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = CircleShape
                        )
                )
                Text(
                    text = item.date.format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy",
                            Locale.getDefault()
                        )
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }
        Text(
            text = formatCurrency(item.amount),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (item.type == TransactionType.EXPENSE
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
        )
    }
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
    categoryIconIdentifier = "food",
    accountId = "acc1",
    accountName = "Cash",
    accountIconIdentifier = "cash",
)

private val previewIncomeTransaction = TransactionItem(
    id = "trx2",
    description = "Gaji Bulanan",
    amount = BigDecimal("7500000"),
    type = TransactionType.INCOME,
    date = LocalDateTime.now(),
    categoryId = "cat2",
    categoryName = "Gaji",
    categoryIconIdentifier = "salary",
    accountId = "acc2",
    accountName = "Bank",
    accountIconIdentifier = "bank",
)

// --- FUNGSI PREVIEW ---

@Preview(name = "Recent Transaction Rows - Light", showBackground = true)
@Preview(
    name = "Recent Transaction Rows - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun RecentTransactionRowPreview() {
    TrackFundsTheme {
        // Gunakan Column untuk menumpuk beberapa contoh
        Column {
            RecentTransactionRow(
                item = previewExpenseTransaction,
            )
            HorizontalDivider()
            RecentTransactionRow(
                item = previewIncomeTransaction,
            )
        }
    }
}