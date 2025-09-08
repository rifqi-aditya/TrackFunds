package com.rifqi.trackfunds.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

// ---------------------------------------------------------------------
// Extended colors (brand & charts)
// ---------------------------------------------------------------------

@androidx.compose.runtime.Stable
data class ExtendedColors(
    val income: Color,
    val expense: Color,
    val accent: Color,
    val onAccent: Color,
    val chartConditionGood: Color,
    val chartConditionWarning: Color,
    val chartConditionNeutral: Color,
    val chartCategory1: Color,
    val chartCategory2: Color,
    val chartCategory3: Color,
    val chartCategory4: Color,
    val chartCategory5: Color,
    val chartCategory6: Color
)

private val LocalExtendedColors = staticCompositionLocalOf<ExtendedColors> {
    error("ExtendedColors not provided. Wrap content in TrackFundsTheme.")
}

// ---------------------------------------------------------------------
// Public Theme API
// ---------------------------------------------------------------------

@Composable
fun TrackFundsTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    dynamicColor: Boolean = true, // auto-disabled < Android 12
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> systemDark
    }

    val context = LocalContext.current
    val colorScheme = remember(isDark, dynamicColor, context) {
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (isDark) darkScheme else lightScheme
        }
    }

    val extendedColors = remember(isDark, dynamicColor, colorScheme) {
        ExtendedColors(
            income = if (isDark) IncomeDark else IncomeLight,
            expense = if (isDark) ExpenseDark else ExpenseLight,
            // Brand accent by default; if dynamicColor, harmonize with scheme.primary
            accent = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                colorScheme.primary
            else
                if (isDark) AccentGreenDark else AccentGreenLight,
            onAccent = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                colorScheme.onPrimary
            else
                OnAccentGreen,
            chartConditionGood = ChartConditionGood,
            chartConditionWarning = ChartConditionWarning,
            chartConditionNeutral = if (isDark) ChartConditionNeutralDark else ChartConditionNeutralLight,
            chartCategory1 = ChartCategory1,
            chartCategory2 = ChartCategory2,
            chartCategory3 = ChartCategory3,
            chartCategory4 = ChartCategory4,
            chartCategory5 = ChartCategory5,
            chartCategory6 = ChartCategory6
        )
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

// Backward-compat overload (optional)
@Deprecated("Use TrackFundsTheme(appTheme = …)")
@Composable
fun TrackFundsTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) = TrackFundsTheme(
    appTheme = if (darkTheme) AppTheme.DARK else AppTheme.LIGHT,
    dynamicColor = dynamicColor,
    content = content
)

object TrackFundsTheme {
    val extendedColors: ExtendedColors
        @Composable get() = LocalExtendedColors.current

    val colorScheme: ColorScheme
        @Composable get() = MaterialTheme.colorScheme
}