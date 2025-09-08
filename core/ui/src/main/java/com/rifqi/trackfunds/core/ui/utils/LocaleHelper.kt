package com.rifqi.trackfunds.core.ui.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LocaleHelper {
    /** langTag: "en" atau "id" */
    fun setAppLocale(langTag: String?) {
        val tags = if (langTag.isNullOrBlank()) "" else langTag
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tags))
    }

    fun currentLangTag(): String {
        val tags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        return if (tags.isNullOrBlank()) "en" else tags
    }
}