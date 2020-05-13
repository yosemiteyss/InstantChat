package com.yosemitedev.instantchat

import android.app.Application
import com.yosemitedev.instantchat.di.AppComponent
import com.yosemitedev.instantchat.di.DaggerAppComponent
import com.yosemitedev.instantchat.utils.PreferencesHelper
import com.yosemitedev.instantchat.utils.Theme
import com.yosemitedev.instantchat.utils.ThemeHelper
import com.yosemitedev.instantchat.utils.WAClientHelper
import javax.inject.Inject

class InstantChat : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    @Inject
    lateinit var prefsHelper: PreferencesHelper

    @Inject
    lateinit var themeHelper: ThemeHelper

    @Inject
    lateinit var clientHelper: WAClientHelper

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        setDefaultTheme()
        setDefaultWAClient()
    }

    private fun setDefaultTheme() {
        // Apply saved theme when the application starts.
        // If there is no saved theme, get default themeKey and save to preferences.
        var themeKey = prefsHelper.theme
        if (themeKey.isNullOrEmpty()) {
            themeKey = themeHelper.getDefaultTheme().key
            prefsHelper.theme = themeKey
        }
        themeHelper.applyTheme(Theme.values().first { it.key == themeKey })
    }

    private fun setDefaultWAClient() {
        // Saved default package name
        val clientPackageName = prefsHelper.defaultClient
        if (clientPackageName.isNullOrEmpty()) {
            prefsHelper.defaultClient = clientHelper.getDefaultClient().packageName
        }
    }
}