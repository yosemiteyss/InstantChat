package com.yosemitedev.instantchat.ui.overview

import com.yosemitedev.instantchat.di.ChildFragmentScope
import com.yosemitedev.instantchat.ui.overview.contact.ContactFragment
import com.yosemitedev.instantchat.ui.overview.settings.SettingsChildFragmentsModule
import com.yosemitedev.instantchat.ui.overview.settings.SettingsFragment
import com.yosemitedev.instantchat.ui.overview.settings.SettingsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class OverviewChildFragmentsModule {

    @ChildFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeContactFragment(): ContactFragment

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = [SettingsModule::class, SettingsChildFragmentsModule::class])
    abstract fun contributeSettingsFragment(): SettingsFragment
}