package com.yosemitedev.instantchat.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_KEY_THEME = "pref_key_theme"
private const val PREF_KEY_CLIENT = "pref_key_client_pkg_name"
private const val PREF_KEY_SHOW_KEYBOARD = "pref_key_show_keyboard"

@Singleton
class PreferencesHelper @Inject constructor(context: Context) {

    private val defaultSharedPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val prefsEditor: SharedPreferences.Editor by lazy { defaultSharedPrefs.edit() }

    var themeKey: String? = null
        get() = defaultSharedPrefs.getString(PREF_KEY_THEME, null)
        set(value) {
            field = value
            prefsEditor.putString(PREF_KEY_THEME, value).apply()
        }

    var clientPackageName: String? = null
        get() = defaultSharedPrefs.getString(PREF_KEY_CLIENT, null)
        set(value) {
            field = value
            prefsEditor.putString(PREF_KEY_CLIENT, value).apply()
        }

    var showKeyboard: Boolean = false
        get() = defaultSharedPrefs.getBoolean(PREF_KEY_SHOW_KEYBOARD, false)
        set(value) {
            field = value
            prefsEditor.putBoolean(PREF_KEY_SHOW_KEYBOARD, value).apply()
        }
}