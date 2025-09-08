package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import javax.inject.Inject

interface SetLocaleUseCase {
    operator fun invoke(tag: String)
}

class SetLocaleUseCaseImpl @Inject constructor(
    private val prefs: AppPrefsRepository
) : SetLocaleUseCase {

    // PERBAIKAN: Fungsi invoke tidak perlu suspend lagi
    override operator fun invoke(tag: String) {
        val toSave = normalize(tag)
        prefs.setLocale(toSave)
    }

    private fun normalize(tag: String): String {
        val normalized = tag.trim().lowercase()

        return when {
            // Jika kosong, kembalikan kosong (ikuti sistem)
            normalized.isEmpty() -> ""
            // Ganti "in" dengan "id"
            normalized == "in" -> "id"
            // Validasi jika tag bahasa valid. Jika tidak, kembalikan kosong.
            java.util.Locale.forLanguageTag(normalized).language.isEmpty() -> ""
            // Jika semua valid, gunakan tag yang sudah dinormalisasi
            else -> normalized
        }
    }
}