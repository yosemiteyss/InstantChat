package com.yosemitedev.instantchat

import android.util.Log
import com.yosemitedev.instantchat.di.DaggerAppComponent
import com.yosemitedev.instantchat.utils.PreferencesHelper
import com.yosemitedev.instantchat.utils.Theme
import com.yosemitedev.instantchat.utils.ThemeHelper
import com.yosemitedev.instantchat.utils.WAClientHelper
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class InstantChat : DaggerApplication() {

    @Inject
    lateinit var prefsHelper: PreferencesHelper

    @Inject
    lateinit var themeHelper: ThemeHelper

    @Inject
    lateinit var clientHelper: WAClientHelper

    override fun onCreate() {
        super.onCreate()
        // Apply saved theme when the application starts.
        // If there is no saved theme, get default themeKey and save to preferences.
        var themeKey = prefsHelper.themeKey
        if (themeKey.isNullOrEmpty()) {
            themeKey = themeHelper.getDefaultTheme().key
            prefsHelper.themeKey = themeKey
        }
        themeHelper.applyTheme(Theme.values().first { it.key == themeKey })

        // Saved default package name
        val clientPackageName = prefsHelper.clientPackageName
        if (clientPackageName.isNullOrEmpty()) {
            prefsHelper.clientPackageName = clientHelper.getDefaultClient().packageName
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}