package com.yosemitedev.instantchat.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.managers.PreferencesManager
import com.yosemitedev.instantchat.managers.ThemeManager
import com.yosemitedev.instantchat.managers.WAClientManager
import com.yosemitedev.instantchat.utils.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel @ViewModelInject constructor(
    preferencesManager: PreferencesManager,
    themeManager: ThemeManager,
    waClientManager: WAClientManager
) : ViewModel() {

    private val _navigateToMain = SingleLiveEvent<Unit>()
    val navigateToMain: LiveData<Unit>
        get() = _navigateToMain

    init {
        viewModelScope.launch {
            preferencesManager.getIsFirstLaunch().collect { isFirstLaunch ->
                if (isFirstLaunch) {
                    preferencesManager.updateDefaultTheme(
                        theme = themeManager.getDefaultTheme().key
                    )
                    preferencesManager.updateDefaultWAClient(
                        waClient = waClientManager.getDefaultClient().packageName
                    )
                    preferencesManager.updateIsFirstLaunch(isFirstLaunch = false)
                }

                _navigateToMain.value = Unit
            }
        }
    }
}