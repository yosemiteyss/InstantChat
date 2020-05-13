package com.yosemitedev.instantchat.di

import com.yosemitedev.instantchat.ui.contact.ContactComponent
import com.yosemitedev.instantchat.ui.splash.SplashComponent
import dagger.Module

@Module(
    subcomponents = [
        SplashComponent::class,
        ContactComponent::class
    ]
)
object SubComponentsModule