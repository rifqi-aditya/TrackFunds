package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal

@Composable
fun InsightCard(
    totalBalance: BigDecimal,
    totalSavings: BigDecimal,
    totalAccounts: Int,
    onBalanceClick: () -> Unit,
    onSavingsClick: () -> Unit,
    onAccountsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            InfoItem(
                label = "Total Balance",
                value = formatCurrency(totalBalance),
                iconIdentifier = "money",
                onClick = onBalanceClick,
                modifier = Modifier.weight(1f)
            )
            InfoItem(
                label = "Savings",
                value = formatCurrency(totalSavings),
                iconIdentifier = "savings",
                onClick = onSavingsClick,
                modifier = Modifier.weight(1f)
            )
            InfoItem(
                label = "Accounts",
                value = "$totalAccounts Account",
                iconIdentifier = "wallet_account",
                onClick = onAccountsClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    iconIdentifier: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DisplayIconFromResource(
            identifier = iconIdentifier,
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
        )
        Text(label, style = MaterialTheme.typography.labelMedium)
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BalanceCardNewPreview() {
    TrackFundsTheme {
        InsightCard(
            totalBalance = BigDecimal("12500000"),
            totalSavings = BigDecimal("5000000"),
            totalAccounts = 4,
            onBalanceClick = {},
            onSavingsClick = {},
            onAccountsClick = {},
        )
    }
}