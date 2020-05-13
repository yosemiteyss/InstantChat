package com.yosemitedev.instantchat.ui.contact

import com.yosemitedev.instantchat.di.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ContactModule::class,
        ContactViewModelModule::class
    ]
)
interface ContactComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ContactComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(contactFragment: ContactFragment)

    fun inject(contactEditFragment: ContactEditFragment)
}