package com.yosemitedev.instantchat.repository

import android.content.Intent
import com.yosemitedev.instantchat.utils.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val prefsHelper: PreferencesHelper,
    private val waClientHelper: WAClientHelper,
    private val themeHelper: ThemeHelper
) {

    fun getShowKeyboard(): Boolean {
        return prefsHelper.showKeyboard
    }

    // Themes
    fun getThemes(): List<Theme> {
        return themeHelper.getThemes()
    }

    fun setTheme(theme: Theme) {
        prefsHelper.theme = theme.key
        themeHelper.applyTheme(theme)
    }

    // WAClients
    fun getWAClients(): List<WAClient> {
        return waClientHelper.getClients()
    }

    fun setDefaultWAClient(client: WAClient) {
        prefsHelper.defaultClient = client.packageName
    }

    fun getWAClientIntent(
        countryCode: String,
        phoneNumber: String
    ): Intent {
        val waClient = WAClient.values().first {
            it.packageName == prefsHelper.defaultClient
        }

        return waClientHelper.getIntent(waClient, countryCode, phoneNumber)
    }
}