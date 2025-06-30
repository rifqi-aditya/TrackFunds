package com.rifqi.trackfunds.feature.transaction.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateTimeDisplayRow(
    selectedDate: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter =
        remember { DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale("en", "EN")) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DisplayIconFromResource(
            identifier = "calendar",
            contentDescription = "Select Date",
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${selectedDate.format(dateFormatter)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "DateTimeDisplayRow - Light")
@Composable
fun DateTimeDisplayRowLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        Surface {
            DateTimeDisplayRow(
                selectedDate = LocalDate.now(),
                onClick = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "DateTimeDisplayRow - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun DateTimeDisplayRowDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface {
            DateTimeDisplayRow(
                selectedDate = LocalDate.of(2025, 12, 25),
                onClick = {}
            )
        }
    }
}