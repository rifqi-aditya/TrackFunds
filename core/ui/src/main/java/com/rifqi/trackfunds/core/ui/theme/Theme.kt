package com.rifqi.trackfunds.core.ui.theme
import BackgroundDark
import BackgroundLight
import BrandBlue
import BrandBlueContainer
import BrandBlueContainerDark
import BrandBlueDark
import NegativeRed
import NegativeRedContainer
import NegativeRedContainerDark
import NegativeRedDark
import NeutralGray
import NeutralGrayContainer
import NeutralGrayContainerDark
import NeutralGrayDark
import OnBackgroundDark
import OnBackgroundLight
import OnBrandBlue
import OnBrandBlueContainer
import OnBrandBlueContainerDark
import OnBrandBlueDark
import OnNegativeRed
import OnNegativeRedContainer
import OnNegativeRedContainerDark
import OnNegativeRedDark
import OnNeutralGray
import OnNeutralGrayContainer
import OnNeutralGrayContainerDark
import OnNeutralGrayDark
import OnPositiveGreen
import OnPositiveGreenContainer
import OnPositiveGreenContainerDark
import OnPositiveGreenDark
import OnSurfaceDark
import OnSurfaceLight
import OnSurfaceVariantDark
import OnSurfaceVariantLight
import OutlineDark
import OutlineLight
import PositiveGreen
import PositiveGreenContainer
import PositiveGreenContainerDark
import PositiveGreenDark
import SurfaceContainerDark
import SurfaceContainerHighDark
import SurfaceContainerHighestDark
import SurfaceContainerLowDark
import SurfaceContainerLowestDark
import SurfaceDark
import SurfaceLight
import SurfaceVariantDark
import SurfaceVariantLight
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val AppDarkColorScheme = darkColorScheme(
    primary = BrandBlueDark,
    onPrimary = OnBrandBlueDark,
    primaryContainer = BrandBlueContainerDark,
    onPrimaryContainer = OnBrandBlueContainerDark,
    secondary = NeutralGrayDark,
    onSecondary = OnNeutralGrayDark,
    secondaryContainer = NeutralGrayContainerDark,
    onSecondaryContainer = OnNeutralGrayContainerDark,
    tertiary = PositiveGreenDark,
    onTertiary = OnPositiveGreenDark,
    tertiaryContainer = PositiveGreenContainerDark,
    onTertiaryContainer = OnPositiveGreenContainerDark,
    error = NegativeRedDark,
    onError = OnNegativeRedDark,
    errorContainer = NegativeRedContainerDark,
    onErrorContainer = OnNegativeRedContainerDark, // <-- Menggunakan nama yang benar
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainerLowest = SurfaceContainerLowestDark
)

private val AppLightColorScheme = lightColorScheme(
    primary = BrandBlue,
    onPrimary = OnBrandBlue,
    primaryContainer = BrandBlueContainer,
    onPrimaryContainer = OnBrandBlueContainer,
    secondary = NeutralGray,
    onSecondary = OnNeutralGray,
    secondaryContainer = NeutralGrayContainer,
    onSecondaryContainer = OnNeutralGrayContainer,
    tertiary = PositiveGreen,
    onTertiary = OnPositiveGreen,
    tertiaryContainer = PositiveGreenContainer,
    onTertiaryContainer = OnPositiveGreenContainer,
    error = NegativeRed,
    onError = OnNegativeRed,
    errorContainer = NegativeRedContainer,
    onErrorContainer = OnNegativeRedContainer, // <-- Menggunakan nama yang benar
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

@Composable
fun TrackFundsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set ke false untuk menggunakan skema warna kustom kita
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> AppDarkColorScheme
        else -> AppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Gunakan tipografi yang sudah Anda definisikan
        shapes = AppShapes,         // Gunakan bentuk rounded yang sudah Anda definisikan
        content = content
    )
}