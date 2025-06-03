package com.rifqi.trackfunds.core.ui.theme

import androidx.compose.ui.graphics.Color

// Primary (Biru)
val PrimaryLight = Color(0xFF0061A4) // Biru yang sedikit lebih cerah dan modern
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFFD1E4FF)
val OnPrimaryContainerLight = Color(0xFF001D36)

val PrimaryDark = Color(0xFF9ECAFF) // Biru muda untuk Dark Mode
val OnPrimaryDark = Color(0xFF003258)
val PrimaryContainerDark = Color(0xFF00497D)
val OnPrimaryContainerDark = Color(0xFFD1E4FF)

// Secondary (Netral/Abu-abu - untuk elemen pendukung)
val SecondaryLight = Color(0xFF535F70)
val OnSecondaryLight = Color(0xFFFFFFFF)
val SecondaryContainerLight = Color(0xFFD7E3F8)
val OnSecondaryContainerLight = Color(0xFF101C2B)

val SecondaryDark = Color(0xFFBBC7DB)
val OnSecondaryDark = Color(0xFF253141)
val SecondaryContainerDark = Color(0xFF3C4758)
val OnSecondaryContainerDark = Color(0xFFD7E3F8)

// Tertiary (Hijau - untuk Pemasukan atau aksen positif)
val TertiaryLight = Color(0xFF006D3B) // Hijau yang lebih standar Material
val OnTertiaryLight = Color(0xFFFFFFFF)
val TertiaryContainerLight = Color(0xFF9AF6B6)
val OnTertiaryContainerLight = Color(0xFF00210E)

val TertiaryDark = Color(0xFF7FDD9B) // Hijau muda untuk Dark Mode
val OnTertiaryDark = Color(0xFF00391C)
val TertiaryContainerDark = Color(0xFF00522B)
val OnTertiaryContainerDark = Color(0xFF9AF6B6)

// Error (Merah)
val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

// Background and Surface
val BackgroundLight = Color(0xFFF8F9FD) // Warna Anda, bagus untuk light mode
val OnBackgroundLight = Color(0xFF1A1C1E)
val SurfaceLight = Color(0xFFF8F9FD) // Bisa sama atau sedikit beda
val OnSurfaceLight = Color(0xFF1A1C1E)

val BackgroundDark = Color(0xFF121212) // Warna Anda, standar untuk dark mode
val OnBackgroundDark = Color(0xFFE2E2E6)
val SurfaceDark = Color(0xFF1A1C1E) // Sedikit lebih terang dari background untuk elevasi
val OnSurfaceDark = Color(0xFFE2E2E6)

// Surface Variants (untuk Card, Chip, dll.)
val SurfaceVariantLight = Color(0xFFE0E4F1) // Dari Gray40 Anda, bagus untuk card light
val OnSurfaceVariantLight = Color(0xFF43474E) // Teks di atas SurfaceVariantLight

val SurfaceVariantDark = Color(0xFF444A63) // Dari Gray80 Anda, bagus untuk card dark
val OnSurfaceVariantDark = Color(0xFFC4C6CF) // Teks di atas SurfaceVariantDark

// Outline
val OutlineLight = Color(0xFF74777F)
val OutlineDark = Color(0xFF8E9099)

// Warna Kustom Anda (bisa didefinisikan di sini atau sebagai CompositionLocal)
val SubtitleTextLight = Color(0xFF6D6D6D) // Dari SubtitleLight Anda
val SubtitleTextDark = Color(0xFFB0B3C1)  // Dari SubtitleDark Anda

// Untuk "Button Accent" Anda, bisa jadi ini adalah warna Primary atau Tertiary,
// atau warna kustom jika memang sangat spesifik.
// Mari kita coba gunakan Primary untuk ini agar konsisten,
// atau Anda bisa definisikan warna kustom jika Primary/Tertiary tidak cocok.
val AccentButtonColorLight = PrimaryLight // Menggunakan Primary
val AccentButtonColorDark = PrimaryDark
