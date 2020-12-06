package com.yosemitedev.instantchat.managers

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_THEME
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_WA_CLIENT
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.IS_FIRST_LAUNCH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val themeManager: ThemeManager,
    private val waClientManager: WAClientManager
) {

    // All DataStore operations run in Dispatchers.IO

    fun getIsFirstLaunch(): Flow<Boolean> = dataStore.data
        .map { preferences ->
            val key = preferencesKey<Boolean>(IS_FIRST_LAUNCH)
            preferences[key] ?: false
        }

    suspend fun updateIsFirstLaunch(isFirstLaunch: Boolean) {
        dataStore.edit { preferences ->
            val key = preferencesKey<Boolean>(IS_FIRST_LAUNCH)
            preferences[key] = isFirstLaunch
        }
    }

    fun getDefaultTheme(): Flow<String> = dataStore.data
        .map { preferences ->
            val key = preferencesKey<String>(DEFAULT_THEME)
            preferences[key] ?: themeManager.getDefaultTheme().key
        }

    suspend fun updateDefaultTheme(theme: String) {
        dataStore.edit { preferences ->
            val key = preferencesKey<String>(DEFAULT_THEME)
            preferences[key] = theme
        }
    }

    fun getDefaultWAClient(): Flow<String> = dataStore.data
        .map { preferences ->
            val key = preferencesKey<String>(DEFAULT_WA_CLIENT)
            preferences[key] ?:  waClientManager.getDefaultClient().packageName
        }

    suspend fun updateDefaultWAClient(waClient: String) {
        dataStore.edit { preferences ->
            val key = preferencesKey<String>(DEFAULT_WA_CLIENT)
            preferences[key] = waClient
        }
    }

    companion object {
        const val PREF_NAME = "instant_chat_prefs"
    }
}