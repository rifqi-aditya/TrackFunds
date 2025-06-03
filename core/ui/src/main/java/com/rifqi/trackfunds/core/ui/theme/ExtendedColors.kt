package com.rifqi.trackfunds.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class CustomColors(
    val subtitle: Color,
    val detailButton: Color
)

val LocalCustomColors = compositionLocalOf {
    CustomColors(
        subtitle = Color.Unspecified,
        detailButton = Color.Unspecified
    )
}

val MaterialTheme.customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current

