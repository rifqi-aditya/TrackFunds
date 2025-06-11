package com.rifqi.trackfunds.feature.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppBar(
    onSettingsClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text("Profile", style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp),
            )
        },
        actions = {
            IconButton(onClick = onSettingsClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        windowInsets = TopAppBarDefaults.windowInsets,
        modifier = Modifier.padding(
            horizontal = 16.dp
        )
    )
}

// --- Preview untuk ProfileTopAppBar ---
@Preview(showBackground = true, name = "Profile TopAppBar Light")
@Composable
private fun ProfileTopAppBarLightPreview() {
    TrackFundsTheme {
        Surface {
            ProfileTopAppBar(onSettingsClick = {})
        }
    }
}

@Preview(
    showBackground = true,
    name = "Profile TopAppBar Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ProfileTopAppBarDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface {
            ProfileTopAppBar(onSettingsClick = {})
        }
    }
}