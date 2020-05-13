package com.yosemitedev.instantchat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.repository.SettingsRepository
import com.yosemitedev.instantchat.utils.Theme
import com.yosemitedev.instantchat.utils.WAClient
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val themes: List<Theme>
        get() = settingsRepository.getThemes()

    val waClients: List<WAClient>
        get() = settingsRepository.getWAClients()

    fun deleteAllContacts() {
        viewModelScope.launch {
            contactRepository.deleteAllContacts()
        }
    }

    fun setTheme(themeKey: String) {
        val theme = Theme.values().first { it.key == themeKey }
        settingsRepository.setTheme(theme)
    }

    fun setClient(packageName: String) {
        val waClient = WAClient.values().first { it.packageName == packageName }
        settingsRepository.setDefaultWAClient(waClient)
    }
}