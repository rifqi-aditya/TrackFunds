package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    isFullScreen: Boolean = false,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface
    )
) {
    val titleStyle = if (isFullScreen) {
        MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    } else {
        MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    }

    TopAppBar(
        title = {
            val titleModifier = if (isFullScreen) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
            }

            Box(
                modifier = titleModifier,
                contentAlignment = if (isFullScreen) Alignment.Center else Alignment.CenterStart
            ) {
                Text(text = title, style = titleStyle)
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(onClick = onNavigateBack) {
                    DisplayIconFromResource(
                        identifier = "arrow_back",
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = actions,
        colors = colors,
        windowInsets = TopAppBarDefaults.windowInsets
    )
}