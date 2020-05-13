package com.yosemitedev.instantchat.ui.settings

import com.yosemitedev.instantchat.di.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(
    modules = [
        SettingsViewModelModule::class
    ]
)
interface SettingsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(preferenceFragment: PreferenceFragment)
}