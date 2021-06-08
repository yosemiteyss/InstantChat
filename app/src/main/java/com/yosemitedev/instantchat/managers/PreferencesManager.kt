package com.yosemitedev.instantchat.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_THEME
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_WA_CLIENT
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.IS_FIRST_LAUNCH
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val themeManager: ThemeManager,
    private val waClientManager: WAClientManager
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

    fun getIsFirstLaunch(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            val key = booleanPreferencesKey(IS_FIRST_LAUNCH)
            preferences[key] ?: false
        }

    suspend fun updateIsFirstLaunch(isFirstLaunch: Boolean) {
        context.dataStore.edit { preferences ->
            val key = booleanPreferencesKey(IS_FIRST_LAUNCH)
            preferences[key] = isFirstLaunch
        }
    }

    fun getDefaultTheme(): Flow<String> = context.dataStore.data
        .map { preferences ->
            val key = stringPreferencesKey(DEFAULT_THEME)
            preferences[key] ?: themeManager.getDefaultTheme().key
        }

    suspend fun updateDefaultTheme(theme: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(DEFAULT_THEME)
            preferences[key] = theme
        }
    }

    fun getDefaultWAClient(): Flow<String> = context.dataStore.data
        .map { preferences ->
            val key = stringPreferencesKey(DEFAULT_WA_CLIENT)
            preferences[key] ?:  waClientManager.getDefaultClient().packageName
        }

    suspend fun updateDefaultWAClient(waClient: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(DEFAULT_WA_CLIENT)
            preferences[key] = waClient
        }
    }

    companion object {
        const val PREF_NAME = "instant_chat_prefs"
    }
}