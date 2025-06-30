package com.rifqi.trackfunds.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Wadah untuk warna-warna kustom kita
data class ExtendedColorScheme(
    val income: Color,
    val onIncomeContainer: Color,
    val incomeContainer: Color,
    val expense: Color,
    val onExpenseContainer: Color,
    val expenseContainer: Color
)

// Sediakan warna default untuk CompositionLocal
val LocalExtendedColorScheme = staticCompositionLocalOf {
    ExtendedColorScheme(
        income = Color.Unspecified,
        onIncomeContainer = Color.Unspecified,
        incomeContainer = Color.Unspecified,
        expense = Color.Unspecified,
        onExpenseContainer = Color.Unspecified,
        expenseContainer = Color.Unspecified
    )
}

// Extension property untuk akses mudah: MaterialTheme.extendedColors
val MaterialTheme.extendedColors: ExtendedColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColorScheme.current