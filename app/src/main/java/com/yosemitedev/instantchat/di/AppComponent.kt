package com.yosemitedev.instantchat.di

import android.content.Context
import com.yosemitedev.instantchat.InstantChat
import com.yosemitedev.instantchat.ui.contact.ContactComponent
import com.yosemitedev.instantchat.ui.settings.SettingsComponent
import com.yosemitedev.instantchat.ui.splash.SplashComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        SubComponentsModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(instantChat: InstantChat)

    fun splashComponent(): SplashComponent.Factory

    fun contactComponent(): ContactComponent.Factory

    fun settingsComponent(): SettingsComponent.Factory
}