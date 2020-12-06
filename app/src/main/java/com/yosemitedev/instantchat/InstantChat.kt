package com.yosemitedev.instantchat

import android.app.Application
import com.yosemitedev.instantchat.managers.PreferencesManager
import com.yosemitedev.instantchat.managers.Theme
import com.yosemitedev.instantchat.managers.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class InstantChat : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Inject
    lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()

        // Apply application-wide theme
        applyTheme()
    }

    private fun applyTheme() = applicationScope.launch {
        preferencesManager.getDefaultTheme().collect { theme ->
            themeManager.applyTheme(
                Theme.values().first { it.key == theme }
            )
        }
    }
}