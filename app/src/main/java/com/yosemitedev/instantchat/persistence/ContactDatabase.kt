package com.yosemitedev.instantchat.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yosemitedev.instantchat.model.Contact

@Database(
    entities = [Contact::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        const val DB_NAME = "record_database"
    }
}