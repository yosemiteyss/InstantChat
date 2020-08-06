package com.yosemitedev.instantchat.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.*
import com.yosemitedev.instantchat.repository.ContactRepository
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
    private val contactRepository: ContactRepository,
    private val preferencesManager: PreferencesManager,
    private val themeManager: ThemeManager,
    private val waClientManager: WAClientManager
) : ViewModel() {

    val themes: List<Theme>
        get() = themeManager.getThemes()

    val waClients: List<WAClient>
        get() = waClientManager.getClients()

    fun deleteAllContacts() {
        viewModelScope.launch {
            contactRepository.deleteAllContacts()
        }
    }

    fun setTheme(themeKey: String) {
        val theme = Theme.values().first { it.key == themeKey }
        themeManager.applyTheme(theme)
    }

    fun setClient(packageName: String) {
        preferencesManager.defaultClient = packageName
    }
}