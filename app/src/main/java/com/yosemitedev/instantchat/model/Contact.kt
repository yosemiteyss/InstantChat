package com.yosemitedev.instantchat.model

import android.content.Context
import android.os.Parcelable
import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.yosemitedev.instantchat.R
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(primaryKeys = ["country_code", "phone_num"])
@Parcelize
data class Contact(
    @ColumnInfo(name = "country_code") val countryCode: String,
    @ColumnInfo(name = "phone_num") val phoneNum: String,
    @ColumnInfo(name = "access_date") val accessDate: Date,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "avatar_res") @DrawableRes val avatarRes: Int,
    @ColumnInfo(name = "type") val type: ContactType
) : Parcelable

fun Contact.getRelativeTimeSpan(): String {
    return DateUtils.getRelativeTimeSpanString(
        this.accessDate.time,
        Date().time,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}

enum class ContactType {
    PRIVATE {
        override fun getTitle(context: Context): String {
            return context.getString(R.string.contact_type_private_title)
        }
    },

    WORK {
        override fun getTitle(context: Context): String {
            return context.getString(R.string.contact_type_work_title)
        }
    };

    abstract fun getTitle(context: Context): String
}
