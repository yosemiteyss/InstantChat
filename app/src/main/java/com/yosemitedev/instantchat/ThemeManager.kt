package com.yosemitedev.instantchat

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor() {

    fun getThemes(): List<Theme> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            listOf(
                Theme.LIGHT,
                Theme.DARK,
                Theme.SYSTEM
            )
        else
            listOf(
                Theme.LIGHT,
                Theme.DARK,
                Theme.BATTERY_SAVER
            )
    }

    fun getDefaultTheme(): Theme {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Theme.SYSTEM
        else
            Theme.BATTERY_SAVER
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