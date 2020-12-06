package com.yosemitedev.instantchat.managers

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.managers.Theme.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor() {

    fun getThemes(): List<Theme> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            listOf(LIGHT, DARK, SYSTEM)
        } else {
            listOf(LIGHT, DARK, BATTERY_SAVER)
        }
    }

    fun getDefaultTheme(): Theme {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SYSTEM
        } else {
            BATTERY_SAVER
        }
    }

    fun applyTheme(theme: Theme) {
        AppCompatDelegate.setDefaultNightMode(theme.mode)
    }
}

enum class Theme(val key: String, val mode: Int) {
    LIGHT("light", AppCompatDelegate.MODE_NIGHT_NO),
    DARK("dark", AppCompatDelegate.MODE_NIGHT_YES),
    SYSTEM("system", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
    BATTERY_SAVER("battery_saver", AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
}

fun Theme.getTitle(context: Context): String {
    return when (this) {
        LIGHT -> context.getString(R.string.theme_light_title)
        DARK -> context.getString(R.string.theme_dark_title)
        SYSTEM -> context.getString(R.string.theme_system_title)
        BATTERY_SAVER -> context.getString(R.string.theme_battery_saver_title)
    }
}