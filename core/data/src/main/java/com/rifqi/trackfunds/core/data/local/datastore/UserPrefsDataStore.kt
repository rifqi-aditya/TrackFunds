package com.rifqi.trackfunds.core.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val DS_NAME = "user_prefs"
val Context.userPrefsDataStore by preferencesDataStore(name = DS_NAME)