package com.rifqi.trackfunds.feature.savings.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.savings.ui.model.SavingsGoalUiModel

@Composable
fun SavingsGoalCard(
    modifier: Modifier = Modifier,
    goal: SavingsGoalUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { goal.progress },
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    color = TrackFundsTheme.extendedColors.accent,
                )
                DisplayIconFromResource(
                    identifier = goal.iconIdentifier,
                    contentDescription = "Goal Icon",
                    modifier = Modifier
                        .size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${goal.savedAmountFormatted} of ${goal.targetAmountFormatted}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Remaining: ${goal.remainingAmountFormatted}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    goal.targetDateFormatted?.let { date ->
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Target Date",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavingsGoalCardWithAndWithoutDatePreview() {
    val goalWithDate = SavingsGoalUiModel(
        id = "1",
        name = "Japan Trip",
        progress = 0.75f,
        iconIdentifier = "travel",
        savedAmountFormatted = "Rp22.500.000",
        targetAmountFormatted = "Rp30.000.000",
        remainingAmountFormatted = "Rp7.500.000",
        targetDateFormatted = "29 Jul 2025"
    )

    val goalWithoutDate = SavingsGoalUiModel(
        id = "2",
        name = "Emergency Fund",
        progress = 0.4f,
        iconIdentifier = "savings",
        savedAmountFormatted = "Rp8.000.000",
        targetAmountFormatted = "Rp20.000.000",
        remainingAmountFormatted = "Rp12.000.000",
        targetDateFormatted = null
    )

    TrackFundsTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SavingsGoalCard(goal = goalWithDate, onClick = {})
            SavingsGoalCard(goal = goalWithoutDate, onClick = {})
        }
    }
}

