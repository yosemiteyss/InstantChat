package com.yosemitedev.instantchat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class InstantChat : Application() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()

        // Pre-populate shared preferences
        preferencesManager.init()

        // Apply application-wide theme
        applyTheme()
    }

    private fun applyTheme() {
        val themeKey = preferencesManager.defaultTheme

        themeManager.applyTheme(
            Theme.values().first { it.key == themeKey }
        )
    }
}