package com.yosemitedev.instantchat.db

import androidx.room.TypeConverter
import com.yosemitedev.instantchat.model.ContactType
import java.util.Date

class Converters {

    @TypeConverter
    fun toDate(value: Long?): Date? =
        value?.let { Date(value) }

    @TypeConverter
    fun toLong(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun toContactType(value: Int) =
        ContactType.values()[value]

    @TypeConverter
    fun fromContactType(type: ContactType) =
        type.ordinal
}