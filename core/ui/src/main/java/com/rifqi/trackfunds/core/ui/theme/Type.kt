package com.rifqi.trackfunds.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.rifqi.trackfunds.core.ui.R

// 1. Inisialisasi Penyedia Font Google (Provider)
// Pastikan file res/values/font_certs.xml ada di modul yang sama dengan R ini (yaitu :core:ui)
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs // Ini dari file font_certs.xml Anda
)

// 2. Definisikan Nama Font Montserrat dari Google Fonts
val montserratFontName = GoogleFont("Montserrat")

// 3. Buat FontFamily untuk Montserrat dengan berbagai ketebalan (weights)
val MontserratFamily = FontFamily(
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Light),       // Weight 300
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Normal),     // Weight 400 (Regular)
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Medium),     // Weight 500
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.SemiBold),   // Weight 600
    Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Bold)        // Weight 700
    // Anda bisa menambahkan lebih banyak weight jika diperlukan dan tersedia di Google Fonts
    // misalnya: FontWeight.ExtraLight, FontWeight.Thin, FontWeight.ExtraBold, FontWeight.Black
    // Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.ExtraBold),
    // Font(googleFont = montserratFontName, fontProvider = provider, weight = FontWeight.Black)
)

// 4. Definisikan AppTypography Anda menggunakan MontserratFamily
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal, // Atau Bold, sesuai preferensi untuk display
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.SemiBold, // SemiBold cocok untuk headline
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium, // Atau SemiBold
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium, // Atau SemiBold
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Normal, // Atau Light
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium, // Atau SemiBold untuk label yang lebih menonjol
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MontserratFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
