package com.rifqi.trackfunds.feature.home.ui.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.util.Calendar

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    userName: String,
    profileImageUrl: String? = null,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onProfileClick) {
            if (profileImageUrl != null) {
                AsyncImage(
                    model = profileImageUrl,
                    // DIUBAH: Menggunakan string resource untuk aksesibilitas
                    contentDescription = stringResource(R.string.home_profile_picture_cd),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    // DIUBAH: Menggunakan string resource untuk aksesibilitas
                    contentDescription = stringResource(R.string.home_default_profile_icon_cd),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                // DIUBAH: Menggunakan fungsi helper untuk sapaan dinamis
                text = stringResource(id = getGreetingResId()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                // DIUBAH: Menggunakan string resource dengan format
                text = stringResource(id = R.string.home_welcome_user, userName),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Row {
        //     IconButton(onClick = onNotificationsClick) {
        //         DisplayIconFromResource(
        //             identifier = "notifications",
        //             contentDescription = stringResource(R.string.home_notifications_cd), // DIUBAH
        //             modifier = Modifier.size(32.dp),
        //             tint = MaterialTheme.colorScheme.onSurface
        //         )
        //     }
        // }
    }
}

@StringRes
private fun getGreetingResId(): Int {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> R.string.home_greeting_morning
        in 12..17 -> R.string.home_greeting_afternoon
        else -> R.string.home_greeting_evening
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderPreview() {
    TrackFundsTheme {
        HomeHeader(
            userName = "Rifqi Aditya",
            onProfileClick = {},
            onNotificationsClick = {},
            profileImageUrl = "https://example.com/profile.jpg",
        )
    }
}