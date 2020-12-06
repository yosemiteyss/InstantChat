package com.yosemitedev.instantchat.ui.settings

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.managers.*
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_THEME
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_WA_CLIENT
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.REMOVE_CONTACTS
import com.yosemitedev.instantchat.ui.settings.SettingsListModel.ActionPreferenceModel
import com.yosemitedev.instantchat.ui.settings.SettingsListModel.ListPreferenceModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val contactRepository: ContactRepository,
    private val preferencesManager: PreferencesManager,
    private val themeManager: ThemeManager,
    private val waClientManager: WAClientManager
) : ViewModel() {

    private val _settingListModels = MutableLiveData<List<SettingsListModel>>()
    val settingsListModel: LiveData<List<SettingsListModel>>
        get() = _settingListModels

    private val defaultClient = preferencesManager.getDefaultWAClient()
    private val defaultTheme = preferencesManager.getDefaultTheme()

    private val preferenceModels = defaultClient.combine(defaultTheme) { defaultClient, defaultTheme ->
        listOf(
            ListPreference(
                key = DEFAULT_WA_CLIENT,
                title = context.getString(R.string.settings_default_client_title),
                drawableRes = R.drawable.ic_whatsapp,
                items = waClientManager.getClients().map {
                    ListPreferenceItem(
                        id = it.packageName,
                        title = it.getTitle(context),
                        value = it.packageName,
                        default = it.packageName == defaultClient
                    )
                }
            ),
            ListPreference(
                key = DEFAULT_THEME,
                title = context.getString(R.string.settings_theme_title),
                drawableRes = R.drawable.ic_format_color_fill,
                items = themeManager.getThemes().map {
                    ListPreferenceItem(
                        id = it.key,
                        title = it.getTitle(context),
                        value = it.key,
                        default = it.key == defaultTheme
                    )
                }
            ),
            ActionPreference(
                key = REMOVE_CONTACTS,
                title = context.getString(R.string.settings_remove_contacts_title),
                drawableRes = R.drawable.ic_account_remove,
                dialogMessage = context.getString(R.string.settings_remove_contacts_message)
            )
        )
    }

    init {
        buildSettingsList()
    }

    private fun buildSettingsList() = viewModelScope.launch {
        preferenceModels.collect { preferences ->
            _settingListModels.value = buildList {
                addAll(preferences.map {
                    when (it) {
                        is ListPreference -> ListPreferenceModel(it)
                        is ActionPreference -> ActionPreferenceModel(it)
                        else -> throw IllegalStateException("Unknown preference type.")
                    }
                })
            }
        }
    }

    fun deleteAllContacts() {
        viewModelScope.launch {
            contactRepository.deleteAllContacts()
        }
    }

    fun updateDefaultTheme(themeKey: String) = viewModelScope.launch {
        val theme = Theme.values().first { it.key == themeKey }
        themeManager.applyTheme(theme)
        preferencesManager.updateDefaultTheme(theme = themeKey)
    }

    fun updateDefaultClient(packageName: String) = viewModelScope.launch {
        preferencesManager.updateDefaultWAClient(waClient = packageName)
    }
}