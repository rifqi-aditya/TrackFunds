package com.rifqi.trackfunds.core.data.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object Prefs {
    object App {
        val ACTIVE_USER_ID = stringPreferencesKey("active_user_id")
        val APP_THEME = stringPreferencesKey("app_theme")     // SYSTEM/LIGHT/DARK
        val LOCALE_TAG = stringPreferencesKey("locale_tag")    // "en" / "id"
    }
}