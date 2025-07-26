package com.rifqi.trackfunds.feature.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.OverlappingIcon
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transactions: TransactionModel,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OverlappingIcon(
            modifier = Modifier
                .size(42.dp),
            mainIconIdentifier = transactions.category?.iconIdentifier,
            badgeIconIdentifier = transactions.account.iconIdentifier
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = transactions.description.ifEmpty { "-" },
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transactions.category?.name ?: "-",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Column(
            modifier = Modifier
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatCurrency(transactions.amount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (transactions.type == TransactionType.EXPENSE
                ) TrackFundsTheme.extendedColors.textExpense else TrackFundsTheme.extendedColors.textIncome
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transactions.date.format(
                    DateTimeFormatter.ofPattern(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

class TransactionItemPreviewParameterProvider : CollectionPreviewParameterProvider<TransactionModel>(
    listOf(
        TransactionModel(
            id = "1",
            description = "Makan Siang",
            amount = BigDecimal("1000000.0"),
            type = TransactionType.EXPENSE,
            date = LocalDateTime.now(),
            account = AccountModel(
                id = "1",
                name = "Dompet Utama",
                iconIdentifier = "ic_wallet",
                balance = BigDecimal("1000000.0"),
            ),
            category = CategoryModel(
                id = "1",
                name = "Makanan",
                iconIdentifier = "ic_food",
                type = TransactionType.EXPENSE
            )
        ),
        TransactionModel(
            id = "2",
            description = "Gaji Bulanan",
            amount = BigDecimal("1000000.0"),
            type = TransactionType.INCOME,
            date = LocalDateTime.now().minusDays(1),
            account = AccountModel(
                id = "2",
                name = "Rekening Bank",
                iconIdentifier = "ic_bank",
                balance = BigDecimal("1000000.0")
            ),
            category = CategoryModel(
                id = "2",
                name = "Gaji",
                iconIdentifier = "ic_salary",
                type = TransactionType.INCOME
            )
        ),
        TransactionModel(
            id = "3",
            description = "Beli Bensin Pertamax Turbo di SPBU dekat rumah untuk perjalanan ke kantor",
            amount = BigDecimal("150000.0"),
            type = TransactionType.EXPENSE,
            date = LocalDateTime.now().minusHours(5),
            account = AccountModel(
                id = "1",
                name = "Dompet Utama Dengan Nama Yang Sangat Panjang Sekali",
                iconIdentifier = "ic_wallet",
                balance = BigDecimal("1000000.0")
            ),
            category = CategoryModel(
                id = "3",
                name = "Transportasi",
                iconIdentifier = "ic_transportation",
                type = TransactionType.EXPENSE
            )
        )
    )
)

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun TransactionItemPreview(
    @PreviewParameter(TransactionItemPreviewParameterProvider::class) transactions: TransactionModel
) {
    TrackFundsTheme {
        TransactionItem(
            modifier = Modifier.padding(16.dp),
            transactions = transactions,
            onClick = {}
        )
    }
}