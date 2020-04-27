package com.yosemitedev.instantchat.ui.overview.settings

import androidx.lifecycle.*
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.utils.PreferencesHelper
import com.yosemitedev.instantchat.utils.Theme
import com.yosemitedev.instantchat.utils.ThemeHelper
import com.yosemitedev.instantchat.utils.WAClient
import com.yosemitedev.instantchat.utils.WAClientHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val prefsHelper: PreferencesHelper,
    private val themeHelper: ThemeHelper,
    private val clientHelper: WAClientHelper,
    private val repository: ContactRepository
) : ViewModel() {

    val themes: LiveData<List<Theme>> = liveData {
        emit(themeHelper.getThemes())
    }

    val clients: LiveData<List<WAClient>> = liveData {
        emit(clientHelper.getClients())
    }

    private val _currentTheme = MutableLiveData<Theme>().apply {
        value = Theme.values().first { it.key == prefsHelper.themeKey }
    }
    val currentTheme: LiveData<Theme>
        get() = _currentTheme

    private val _currentClient = MutableLiveData<WAClient>().apply {
        value = WAClient.values().first { it.packageName == prefsHelper.clientPackageName }
    }
    val currentClient: LiveData<WAClient>
        get() = _currentClient

    private val _showKeyboard = MutableLiveData<Boolean>().apply {
        value = prefsHelper.showKeyboard
    }
    val showKeyboard: LiveData<Boolean>
        get() = _showKeyboard

    fun setTheme(theme: Theme) {
        _currentTheme.value = theme
        prefsHelper.themeKey = theme.key
        themeHelper.applyTheme(theme)
    }

    fun setClient(client: WAClient) {
        _currentClient.value = client
        prefsHelper.clientPackageName = client.packageName
    }

    fun deleteAllContacts() {
        viewModelScope.launch {
            repository.deleteAllContacts()
        }
    }

    fun setShowKeyboard(isShow: Boolean) {
        _showKeyboard.value = isShow
        prefsHelper.showKeyboard = isShow
    }
}