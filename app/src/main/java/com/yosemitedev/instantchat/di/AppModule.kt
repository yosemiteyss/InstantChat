package com.yosemitedev.instantchat.di

import android.content.Context
import androidx.room.Room
import com.yosemitedev.instantchat.persistence.ContactDao
import com.yosemitedev.instantchat.persistence.ContactDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideContactDatabase(context: Context): ContactDatabase =
        Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            ContactDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @JvmStatic
    @Singleton
    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao =
        contactDatabase.contactDao()
}