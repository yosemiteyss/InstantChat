package com.yosemitedev.instantchat.ui.contact

import com.yosemitedev.instantchat.di.ActivityScope
import com.yosemitedev.instantchat.persistence.ContactDao
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.utils.AvatarStore
import dagger.Module
import dagger.Provides

@Module
object ContactModule {

    @JvmStatic
    @ActivityScope
    @Provides
    fun provideAvatarStore() = AvatarStore()

    @JvmStatic
    @ActivityScope
    @Provides
    fun provideContactRepository(contactDao: ContactDao, avatarStore: AvatarStore) =
        ContactRepository(contactDao, avatarStore)
}