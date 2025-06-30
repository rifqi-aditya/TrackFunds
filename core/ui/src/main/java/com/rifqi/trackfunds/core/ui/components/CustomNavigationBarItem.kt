package com.rifqi.trackfunds.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RowScope.CustomNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    // Box utama tetap untuk menangani klik dan mengambil seluruh ruang
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = null, // Ripple tetap non-aktif
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Kolom untuk menata ikon dan label secara vertikal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            // Label sekarang selalu terlihat, warnanya yang berubah
            label()
        }
    }
}