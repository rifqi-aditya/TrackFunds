package com.rifqi.trackfunds.core.domain.utils

fun generateStandardKeyFromName(name: String): String {
    // 1. Ubah ke huruf kecil
    val lowerCase = name.lowercase()

    // 2. Ganti semua spasi (termasuk spasi ganda) dengan satu garis bawah (_)
    val withUnderscores = lowerCase.replace(Regex("\\s+"), "_")

    // 3. Hapus semua karakter yang bukan huruf, angka, atau garis bawah
    return withUnderscores.replace(Regex("[^a-z0-9_]"), "")
}