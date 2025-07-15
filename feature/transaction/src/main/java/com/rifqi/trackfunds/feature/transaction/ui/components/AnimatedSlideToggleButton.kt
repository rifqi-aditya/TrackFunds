package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ToggleItem<T>(
    val item: T,
    val label: String
)

@Composable
fun <T> AnimatedSlideToggleButton(
    items: List<ToggleItem<T>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var itemWidths by remember { mutableStateOf(emptyList<Dp>()) }

    // Inisialisasi lebar item jika belum ada
    if (items.isNotEmpty() && itemWidths.size != items.size) {
        itemWidths = items.map { 0.dp }
    }

    val selectedIndex = items.indexOfFirst { it.item == selectedItem }

    // Hitung offset (posisi X) untuk indikator
    val indicatorOffset by animateDpAsState(
        targetValue = if (selectedIndex != -1) {
            itemWidths.subList(0, selectedIndex).sumOf { it.value.toDouble() }.dp
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 300),
        label = "indicatorOffset"
    )

    // Wadah utama dengan border
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
    ) {
        // Indikator yang bisa bergeser
        Box(
            modifier = Modifier
                .width(if (selectedIndex != -1) itemWidths.getOrElse(selectedIndex) { 0.dp } else 0.dp)
                .fillMaxHeight()
                .padding(vertical = 6.dp, horizontal = 6.dp)
                .offset(x = indicatorOffset)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface)
        )

        // Teks di atas indikator
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, toggleItem ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned {
                            // Simpan lebar setiap item saat diukur
                            if (itemWidths.isNotEmpty()) {
                                itemWidths = itemWidths.toMutableList().also { list ->
                                    list[index] = with(density) { it.size.width.toDp() }
                                }
                            }
                        }
                        .clip(MaterialTheme.shapes.large)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onItemSelected(toggleItem.item) }
                        )
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = toggleItem.label,
                        color = if (selectedIndex == index) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}