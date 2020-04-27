package com.yosemitedev.instantchat.ui.overview

import androidx.lifecycle.ViewModel
import com.yosemitedev.instantchat.di.FragmentScope
import com.yosemitedev.instantchat.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class OverviewModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [OverviewChildFragmentsModule::class])
    abstract fun contributeOverviewFragment(): OverviewFragment

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(viewModel: OverviewViewModel): ViewModel
}