package com.rifqi.trackfunds.feature.home.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    userName: String,
    profileImageUrl: String? = null,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onProfileClick) {
            // Cek apakah URL gambar profil tersedia
            if (profileImageUrl != null) {
                // Jika ada, gunakan AsyncImage dari Coil untuk memuat dari internet
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape), // Membuat gambar menjadi lingkaran
                    contentScale = ContentScale.Crop // Memastikan gambar mengisi lingkaran
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Default Profile Icon",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Good Morning",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "Hi, $userName",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

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
            onNotificationsClick = {},
            profileImageUrl = "https://example.com/profile.jpg",
        )
    }
}