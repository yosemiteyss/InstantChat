package com.yosemitedev.instantchat.di

import android.content.Context
import androidx.room.Room
import com.yosemitedev.instantchat.db.ContactDao
import com.yosemitedev.instantchat.db.ContactDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContactDatabase(context: Context): ContactDatabase =
        Room.databaseBuilder(context,
            ContactDatabase::class.java,
            ContactDatabase.DB_NAME)
            .addMigrations(ContactDatabase.MIGRATION_1_2, ContactDatabase.MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideContactDao(contactDatabase: ContactDatabase): ContactDao =
        contactDatabase.contactDao()

}