package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun HomeHeader(
    userName: String,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onProfileClick) {
            Icon(
                Icons.Default.AccountCircle,
                "Profile",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            "Hi, $userName",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
//        Row {
//            IconButton(onClick = onNotificationsClick) {
//                DisplayIconFromResource(
//                    identifier = "notifications",
//                    contentDescription = "Notifications",
//                    modifier = Modifier.size(32.dp),
//                    tint = MaterialTheme.colorScheme.onSurface
//                )
//            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    TrackFundsTheme {
        HomeHeader(
            userName = "Rifqi Aditya",
            onProfileClick = {},
            onNotificationsClick = {}
        )
    }
}