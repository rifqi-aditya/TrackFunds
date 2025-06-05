package com.rifqi.trackfunds.core.ui.theme

import BackgroundDark
import BackgroundLight
import ErrorContainerDark
import ErrorContainerLight
import ErrorDark
import ErrorLight
import OnBackgroundDark
import OnBackgroundLight
import OnErrorContainerDark
import OnErrorContainerLight
import OnErrorDark
import OnErrorLight
import OnPastelBlueContainerDark
import OnPastelBlueContainerLight
import OnPastelBlueDark
import OnPastelBlueLight
import OnPastelMintSecondaryContainerDark
import OnPastelMintSecondaryContainerLight
import OnPastelMintSecondaryDark
import OnPastelMintSecondaryLight
import OnPastelPeachContainerDark
import OnPastelPeachContainerLight
import OnPastelPeachDark
import OnPastelPeachLight
import OnSurfaceDark
import OnSurfaceLight
import OnSurfaceVariantDark
import OnSurfaceVariantLight
import OutlineDark
import OutlineLight
import PastelBlueContainerDark
import PastelBlueContainerLight
import PastelBlueDark
import PastelBlueLight
import PastelMintSecondaryContainerDark
import PastelMintSecondaryContainerLight
import PastelMintSecondaryDark
import PastelMintSecondaryLight
import PastelPeachContainerDark
import PastelPeachContainerLight
import PastelPeachDark
import PastelPeachLight
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
    primary = PastelBlueDark,
    onPrimary = OnPastelBlueDark,
    primaryContainer = PastelBlueContainerDark,
    onPrimaryContainer = OnPastelBlueContainerDark,
    secondary = PastelMintSecondaryDark,
    onSecondary = OnPastelMintSecondaryDark,
    secondaryContainer = PastelMintSecondaryContainerDark,
    onSecondaryContainer = OnPastelMintSecondaryContainerDark,
    tertiary = PastelPeachDark,
    onTertiary = OnPastelPeachDark,
    tertiaryContainer = PastelPeachContainerDark,
    onTertiaryContainer = OnPastelPeachContainerDark,
    error = ErrorDark, // Tetap menggunakan ErrorDark standar
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

private val AppLightColorScheme = lightColorScheme(
    primary = PastelBlueLight,
    onPrimary = OnPastelBlueLight,
    primaryContainer = PastelBlueContainerLight,
    onPrimaryContainer = OnPastelBlueContainerLight,
    secondary = PastelMintSecondaryLight,
    onSecondary = OnPastelMintSecondaryLight,
    secondaryContainer = PastelMintSecondaryContainerLight,
    onSecondaryContainer = OnPastelMintSecondaryContainerLight,
    tertiary = PastelPeachLight,
    onTertiary = OnPastelPeachLight,
    tertiaryContainer = PastelPeachContainerLight,
    onTertiaryContainer = OnPastelPeachContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
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
    dynamicColor: Boolean = false, // Tetap biarkan opsi dynamic color jika diinginkan
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Selalu gunakan skema warna pastel yang sudah kita definisikan
        darkTheme -> AppDarkColorScheme
        else -> AppLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Pastikan AppTypography sudah menggunakan Montserrat
        shapes = AppShapes,         // Pastikan AppShapes Anda mendukung sudut rounded
        content = content
    )
}