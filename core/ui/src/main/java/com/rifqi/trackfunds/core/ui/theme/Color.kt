import androidx.compose.ui.graphics.Color

// --- Primary (Biru Pastel Lembut) ---
val PastelBlueLight = Color(0xFF7EADD1) // Biru yang lebih lembut
val OnPastelBlueLight = Color(0xFFFFFFFF)
val PastelBlueContainerLight = Color(0xFFD1E4FF) // Warna Anda sebelumnya, cocok
val OnPastelBlueContainerLight = Color(0xFF001D36)

val PastelBlueDark = Color(0xFFA8C8FF) // Biru pastel terang untuk Dark Mode
val OnPastelBlueDark = Color(0xFF00315B)
val PastelBlueContainerDark = Color(0xFF004780) // Sedikit lebih gelap untuk kontras
val OnPastelBlueContainerDark = Color(0xFFD6E3FF)

// --- Secondary (Abu-abu Kehijauan Pastel atau Netral Lembut) ---
val PastelMintSecondaryLight = Color(0xFFB2DFDB) // Mint pucat
val OnPastelMintSecondaryLight = Color(0xFF003733)
val PastelMintSecondaryContainerLight = Color(0xFFCDEEEA)
val OnPastelMintSecondaryContainerLight = Color(0xFF00201E)

val PastelMintSecondaryDark = Color(0xFF98C7C3) // Mint sedikit lebih gelap untuk Dark Mode
val OnPastelMintSecondaryDark = Color(0xFF003733)
val PastelMintSecondaryContainerDark = Color(0xFF3A4F4C)
val OnPastelMintSecondaryContainerDark = Color(0xFFB4F4E0)

// --- Tertiary (Peach atau Pink Pastel untuk Aksen Hangat) ---
val PastelPeachLight = Color(0xFFFFB7A4)
val OnPastelPeachLight = Color(0xFF5A1A0B)
val PastelPeachContainerLight = Color(0xFFFFDBCF)
val OnPastelPeachContainerLight = Color(0xFF3E0400)

val PastelPeachDark = Color(0xFFFFB59C)
val OnPastelPeachDark = Color(0xFF5A1A0B)
val PastelPeachContainerDark = Color(0xFF7A2F19)
val OnPastelPeachContainerDark = Color(0xFFFFDBCF)

// --- Error (Tetap gunakan merah yang jelas untuk kontras) ---
val ErrorLight = Color(0xFFBA1A1A)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF690005)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

// --- Background, Surface, dkk. ---
val BackgroundLight = Color(0xFFFDFBFF) // Off-white lembut
val OnBackgroundLight = Color(0xFF1A1C1E) // Hitam lembut
val SurfaceLight = Color(0xFFFDFBFF)
val OnSurfaceLight = Color(0xFF1A1C1E)

val BackgroundDark = Color(0xFF1A1B1F) // Abu-abu sangat tua, bukan hitam pekat
val OnBackgroundDark = Color(0xFFE3E2E6) // Putih keabuan
val SurfaceDark = Color(0xFF2C2E32)  // Sedikit lebih terang dari background untuk elevasi Card/Surface
val OnSurfaceDark = Color(0xFFE3E2E6)

// Surface Variants (untuk Card, Chip, dll. agar ada sedikit perbedaan dari Surface utama)
val SurfaceVariantLight = Color(0xFFF0F2F5) // Abu-abu sangat muda dan sejuk
val OnSurfaceVariantLight = Color(0xFF43474E) // Teks gelap di atasnya

val SurfaceVariantDark = Color(0xFF3A3C40) // Abu-abu tua untuk varian surface
val OnSurfaceVariantDark = Color(0xFFC4C6CF) // Teks terang di atasnya

// Outline
val OutlineLight = Color(0xFF79747E)
val OutlineDark = Color(0xFF938F99)

// Warna Teks Subtitle (opsional, bisa pakai OnSurfaceVariant)
val SubtitleTextLight = OnSurfaceVariantLight.copy(alpha = 0.8f)
val SubtitleTextDark = OnSurfaceVariantDark.copy(alpha = 0.8f)