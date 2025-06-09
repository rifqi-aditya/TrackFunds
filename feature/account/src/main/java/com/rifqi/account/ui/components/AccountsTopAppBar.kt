package com.rifqi.account.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
fun AccountsTopAppBar(
    onSearchClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text("Accounts", style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_wallet),
                contentDescription = "Accounts",
                modifier = Modifier.size(28.dp).padding(end = 8.dp),
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search")
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

// --- Preview untuk AccountsTopAppBar ---
@Preview(showBackground = true, name = "Accounts TopAppBar Light")
@Composable
private fun AccountsTopAppBarLightPreview() {
    TrackFundsTheme {
        Surface {
            AccountsTopAppBar(onSearchClick = {})
        }
    }
}

@Preview(
    showBackground = true,
    name = "Accounts TopAppBar Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AccountsTopAppBarDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        Surface {
            AccountsTopAppBar(onSearchClick = {})
        }
    }
}