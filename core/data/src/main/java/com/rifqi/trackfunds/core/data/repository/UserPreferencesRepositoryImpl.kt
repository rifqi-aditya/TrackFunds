package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Membuat instance DataStore sebagai singleton untuk seluruh aplikasi
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {

    // Kunci untuk menyimpan dan mengakses UID dari DataStore
    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
    }

    /**
     * Menyediakan Flow yang akan emit UID pengguna setiap kali ada perubahan.
     * Akan emit null jika tidak ada UID yang tersimpan.
     */
    override val userUid: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_UID]
        }

    /**
     * Menyimpan UID pengguna ke DataStore.
     */
    override suspend fun saveUserUid(uid: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = uid
        }
    }

    /**
     * Menghapus UID pengguna dari DataStore, biasanya saat logout.
     */
    override suspend fun clearUserUid() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_UID)
        }
    }
}