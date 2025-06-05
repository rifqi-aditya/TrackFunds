package com.rifqi.trackfunds.feature.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    currentMonth: String,
    dateRange: String,
    onCalendarClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onCalendarClick)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "Pilih Periode",
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(currentMonth, style = MaterialTheme.typography.titleMedium)
                    Text(
                        dateRange,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = "Notification",
                )
            }
        },
        windowInsets = TopAppBarDefaults.windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
        )
    )
}

// --- PREVIEWS UNTUK HOMETOPAPPBAR ---

@Preview(showBackground = true, name = "HomeTopAppBar - Light Mode")
@Composable
fun HomeTopAppBarLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        Surface { // Surface agar background preview terlihat
            HomeTopAppBar(
                currentMonth = "June",
                dateRange = "01 - 30 Jun 2025",
                onCalendarClick = {},
                onNotificationsClick = {},
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "HomeTopAppBar - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeTopAppBarDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface { // Surface agar background preview terlihat
            HomeTopAppBar(
                currentMonth = "Juli 2025",
                dateRange = "01 - 31 Juli 2025",
                onCalendarClick = {},
                onNotificationsClick = {},
            )
        }
    }
}