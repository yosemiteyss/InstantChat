package com.yosemitedev.instantchat.di

import android.content.Context
import androidx.room.Room
import com.yosemitedev.instantchat.db.ContactDao
import com.yosemitedev.instantchat.db.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
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

    @Singleton
    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao {
        return contactDatabase.contactDao()
    }
}