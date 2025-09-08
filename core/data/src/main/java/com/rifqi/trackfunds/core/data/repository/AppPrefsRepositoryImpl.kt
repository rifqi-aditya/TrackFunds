package com.rifqi.trackfunds.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rifqi.trackfunds.core.data.di.ApplicationScope
import com.rifqi.trackfunds.core.data.local.datastore.Prefs
import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @ApplicationScope private val appScope: CoroutineScope
) : AppPrefsRepository {

    private val prefsFlow: Flow<Preferences> = dataStore.data

    override val activeUserId: Flow<String?> =
        prefsFlow.map { it[Prefs.App.ACTIVE_USER_ID] }

    override val appTheme: StateFlow<AppTheme> =
        prefsFlow.map { it[Prefs.App.APP_THEME]?.toAppThemeOr(AppTheme.SYSTEM) ?: AppTheme.SYSTEM }
            .stateIn(appScope, SharingStarted.Eagerly, AppTheme.SYSTEM)

    override val localeTag: StateFlow<String> =
        prefsFlow.map { it[Prefs.App.LOCALE_TAG] ?: "" }
            .stateIn(appScope, SharingStarted.Eagerly, "")

    override suspend fun setActiveUserId(id: String) {
        dataStore.edit { it[Prefs.App.ACTIVE_USER_ID] = id }
    }

    override suspend fun clearActiveUserId() {
        dataStore.edit { it.remove(Prefs.App.ACTIVE_USER_ID) }
    }

    override fun setTheme(theme: AppTheme) {
        appScope.launch {
            dataStore.edit { it[Prefs.App.APP_THEME] = theme.name }
        }
    }

    override fun setLocale(tag: String) {
        appScope.launch {
            dataStore.edit { it[Prefs.App.LOCALE_TAG] = tag }
        }
    }

    override suspend fun requireActiveUserId(): String =
        activeUserId.filterNotNull().first()
}

private fun String.toAppThemeOr(default: AppTheme): AppTheme =
    runCatching { AppTheme.valueOf(this) }.getOrElse { default }