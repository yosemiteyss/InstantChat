package com.yosemitedev.instantchat.db

import androidx.room.TypeConverter
import com.yosemitedev.instantchat.model.Category
import java.util.*

class ContactTypeConverters {

    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun categoryToString(category: Category): String {
        return category.title
    }

    @TypeConverter
    fun stringToCategory(value: String): Category {
        return Category.values().first { it.title == value }
    }
}