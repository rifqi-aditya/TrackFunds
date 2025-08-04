package com.rifqi.account.ui.addedit.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@Composable
fun IconPicker(
    iconIdentifier: String,
    iconError: String?,
    onIconClicked: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (iconError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
        label = "iconBorderColor"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .clickable(onClick = onIconClicked),
            contentAlignment = Alignment.Center
        ) {
            if (iconIdentifier.isNotBlank()) {
                DisplayIconFromResource(
                    identifier = iconIdentifier,
                    contentDescription = "Selected Icon",
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null)
                    Text("Select Icon", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        AnimatedVisibility(visible = iconError != null) {
            Text(
                text = iconError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}