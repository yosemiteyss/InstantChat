package com.yosemitedev.instantchat.ui.splash

import com.yosemitedev.instantchat.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        SplashModule::class
    ]
)
interface SplashComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }

    fun inject(splashActivity: SplashActivity)
}