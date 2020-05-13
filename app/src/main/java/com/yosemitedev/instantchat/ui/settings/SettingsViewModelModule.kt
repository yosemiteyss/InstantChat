package com.yosemitedev.instantchat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yosemitedev.instantchat.viewmodel.SettingsViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: SettingsViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @SettingsViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}