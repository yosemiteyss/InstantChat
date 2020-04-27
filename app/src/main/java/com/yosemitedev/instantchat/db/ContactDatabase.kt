package com.yosemitedev.instantchat.db

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.ui.AvatarStore

@Database(
    entities = [Contact::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        const val DB_NAME = "record_database"

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE Contact (
                        country_code TEXT NOT NULL,
                        phone_num TEXT NOT NULL,
                        accessDate INTEGER NOT NULL,
                        name TEXT,
                        avatar INTEGER NOT NULL,
                        type INTEGER NOT NULL,
                        PRIMARY KEY (country_code, phone_num)
                    )
                """)
                database.execSQL("""
                    INSERT INTO Contact (country_code, phone_num, accessDate, name)
                    SELECT prefix, phoneNum, accessDate, contactName FROM PhoneNumRecord
                """)
                database.execSQL("""
                    UPDATE Contact SET type = 0 AND avatar = ${AvatarStore.getRandomAvatar()}
                """)
                database.execSQL("""DROP TABLE PhoneNumRecord""")
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE backup (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        prefix TEXT NOT NULL, 
                        phoneNum TEXT NOT NULL, 
                        accessDate INTEGER NOT NULL, 
                        contactName TEXT NOT NULL, 
                        pin INTEGER NOT NULL DEFAULT 0, 
                        UNIQUE(prefix, phoneNum)
                    )
                """)
                database.execSQL("""
                    INSERT INTO backup (prefix, phoneNum, accessDate) 
                    SELECT prefix, phoneNum, accessDate FROM PhoneNumRecord
                """)
                database.execSQL("DROP TABLE PhoneNumRecord")
                database.execSQL("ALTER TABLE backup RENAME TO PhoneNumRecord")
                database.execSQL("""
                    CREATE UNIQUE INDEX index_PhoneNumRecord_prefix_phoneNum 
                    ON PhoneNumRecord (prefix, phoneNum)
                """)
            }
        }
    }
}