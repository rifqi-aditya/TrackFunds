package com.rifqi.trackfunds.feature.savings.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.ui.components.DynamicProgressBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SavingsGoalRow(
    goal: SavingsGoalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian Atas: Ikon dan Judul
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                DisplayIconFromResource(
                    identifier = goal.iconIdentifier,
                    contentDescription = goal.name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) { // Column ini mengambil sisa ruang
                    Text(
                        goal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    goal.targetDate?.let { date ->
                        Text(
                            // Format tanggal agar lebih mudah dibaca
                            text = "Target: ${date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            DynamicProgressBar(
                progress = goal.progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Bagian Bawah: Detail Nominal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Teks "Terkumpul" di kiri
                Text(
                    text = "Saved: ${formatCurrency(goal.currentAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Teks "Target" di kanan
                Text(
                    text = "Target: ${formatCurrency(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private val previewGoalInProgress = SavingsGoalItem(
    id = "1",
    name = "Liburan ke Jepang",
    targetAmount = BigDecimal("20000000"),
    currentAmount = BigDecimal("7500000"),
    targetDate = LocalDateTime.now().plusMonths(12),
    iconIdentifier = "travel",
    isAchieved = false
)

private val previewGoalAchieved = SavingsGoalItem(
    id = "2",
    name = "DP Rumah",
    targetAmount = BigDecimal("150000000"),
    currentAmount = BigDecimal("165000000"),
    targetDate = LocalDateTime.now().plusYears(3),
    iconIdentifier = "housing",
    isAchieved = true
)

// --- FUNGSI PREVIEW ---
@Preview(name = "Savings Goal Row - Light", showBackground = true)
@Preview(
    name = "Savings Goal Row - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SavingsGoalRowPreview() {
    TrackFundsTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Contoh untuk progres yang sedang berjalan
            SavingsGoalRow(
                goal = previewGoalInProgress,
                onClick = {}
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Contoh untuk tujuan yang sudah tercapai (progres 100%)
            SavingsGoalRow(
                goal = previewGoalAchieved,
                onClick = {}
            )
        }
    }
}