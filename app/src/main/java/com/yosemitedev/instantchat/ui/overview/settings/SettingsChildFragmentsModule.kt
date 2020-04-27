package com.yosemitedev.instantchat.ui.overview.settings

import com.yosemitedev.instantchat.di.NestedChildFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsChildFragmentsModule {

    @NestedChildFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeThemeDialogFragment(): ThemeDialogFragment

    @NestedChildFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeClientDialogFragment(): ClientDialogFragment
}