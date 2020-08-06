package com.yosemitedev.instantchat.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.yosemitedev.instantchat.db.ContactDao
import com.yosemitedev.instantchat.db.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideContactDatabase(
        @ApplicationContext context: Context
    ): ContactDatabase {
        return Room.databaseBuilder(
            context,
            ContactDatabase::class.java,
            ContactDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.contactDao()
    }

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideSharedPreferencesEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}