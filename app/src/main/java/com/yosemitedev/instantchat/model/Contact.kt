package com.yosemitedev.instantchat.model

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
    tableName = "Contact",
    primaryKeys = ["country_code", "phone_num"]
)
@Parcelize
data class Contact(

    @ColumnInfo(name = "country_code")
    val countryCode: String,

    @ColumnInfo(name = "phone_num")
    val phoneNum: String,

    @ColumnInfo(name = "access_date")
    val accessDate: Date,

    @ColumnInfo(name = "category")
    val category: Category,

    @ColumnInfo(name = "avatarRes")
    @DrawableRes
    val avatarRes: Int

) : Parcelable {

    fun getAccessDateRelativeTimeSpan(): String {
        return DateUtils.getRelativeTimeSpanString(
            this.accessDate.time,
            Date().time,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    }
}
