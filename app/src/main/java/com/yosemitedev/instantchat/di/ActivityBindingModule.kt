package com.yosemitedev.instantchat.di

import com.yosemitedev.instantchat.ui.SplashActivity
import com.yosemitedev.instantchat.ui.MainActivity
import com.yosemitedev.instantchat.ui.MainModule
import com.yosemitedev.instantchat.ui.overview.OverviewModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            // Activity
            MainModule::class,
            // Fragments
            OverviewModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity

}