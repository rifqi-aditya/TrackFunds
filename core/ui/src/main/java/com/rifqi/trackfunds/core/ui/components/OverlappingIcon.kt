package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@Composable
fun OverlappingIcon(
    modifier: Modifier = Modifier,
    mainIconIdentifier: String?,
    badgeIconIdentifier: String?,
) {
    Box(modifier = modifier) {
        DisplayIconFromResource(
            identifier = mainIconIdentifier,
            contentDescription = "Main Icon",
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(6.dp)
        )

        // 2. Ikon Kecil/Badge (di lapisan atas)
        DisplayIconFromResource(
            identifier = badgeIconIdentifier,
            contentDescription = "Badge Icon",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(16.dp)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                )
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(3.dp)
        )
    }
}


/**
 * Fungsi Preview
 */
@Preview(showBackground = true)
@Composable
private fun OverlappingIconPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            OverlappingIcon(
                modifier = Modifier.size(56.dp),
                mainIconIdentifier = "wallet",
                badgeIconIdentifier = "cart"
            )
        }
    }
}