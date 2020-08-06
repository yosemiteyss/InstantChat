package com.yosemitedev.instantchat

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesEditor: SharedPreferences.Editor,
    private val themeManager: ThemeManager,
    private val waClientManager: WAClientManager
) {

    private var firstLaunch: Boolean = true
        get() = sharedPreferences.getBoolean(
            context.getString(R.string.pref_key_first_launch),
            true
        )
        set(value) {
            field = value
            sharedPreferencesEditor.putBoolean(
                context.getString(R.string.pref_key_first_launch),
                value
            ).apply()
        }

    var dbPrepopoulated: Boolean = false
        get() = sharedPreferences.getBoolean(
            context.getString(R.string.pref_key_db_prepopulated),
            false
        )
        set(value) {
            field = value
            sharedPreferencesEditor.putBoolean(
                context.getString(R.string.pref_key_db_prepopulated),
                value
            ).apply()
        }

    var navigateToEdit: Boolean = false
        get() = sharedPreferences.getBoolean(
            context.getString(R.string.pref_key_navigate_to_edit),
            false
        )
        set(value) {
            field = value
            sharedPreferencesEditor.putBoolean(
                context.getString(R.string.pref_key_navigate_to_edit),
                value
            ).apply()
        }

    var defaultTheme: String? = null
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_key_default_theme),
            themeManager.getDefaultTheme().key
        )
        set(value) {
            field = value
            sharedPreferencesEditor.putString(
                context.getString(R.string.pref_key_default_theme),
                value
            ).apply()
        }

    var defaultClient: String? = null
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_key_default_client),
            waClientManager.getDefaultClient().packageName
        )
        set(value) {
            field = value
            sharedPreferencesEditor.putString(
                context.getString(R.string.pref_key_default_client),
                value
            ).apply()
        }

    /**
     * Set default value to shared preferences when the application launches the first time
     */
    fun init() {
        if (firstLaunch) {
            navigateToEdit = false
            defaultTheme = themeManager.getDefaultTheme().key
            defaultClient = waClientManager.getDefaultClient().packageName
            firstLaunch = false
        }
    }
}