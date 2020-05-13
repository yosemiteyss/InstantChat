package com.yosemitedev.instantchat.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.yosemitedev.instantchat.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(
    private val context: Context
) {

    var theme: String? = null
        get() = defaultSharedPrefs.getString(
            context.getString(R.string.pref_key_theme),
            null
        )
        set(value) {
            field = value
            prefsEditor.putString(context.getString(R.string.pref_key_theme), value).apply()
        }

    var defaultClient: String? = null
        get() = defaultSharedPrefs.getString(
            context.getString(R.string.pref_key_client),
            null
        )
        set(value) {
            field = value
            prefsEditor.putString(context.getString(R.string.pref_key_client), value).apply()
        }

    var showKeyboard: Boolean = false
        get() = defaultSharedPrefs.getBoolean(
            context.getString(R.string.pref_key_show_keyboard),
            false
        )
        set(value) {
            field = value
            prefsEditor.putBoolean(context.getString(R.string.pref_key_show_keyboard), value)
                .apply()
        }

    private val defaultSharedPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val prefsEditor: SharedPreferences.Editor by lazy {
        defaultSharedPrefs.edit()
    }
}