package com.yosemitedev.instantchat.model

import android.text.format.DateUtils
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity(primaryKeys = ["country_code", "phone_num"])
data class Contact(

    @ColumnInfo(name = "country_code")
    val countryCode: String,

    @ColumnInfo(name = "phone_num")
    val phoneNum: String,

    val accessDate: Date,

    val name: String? = null,

    @DrawableRes
    val avatar: Int,

    val type: ContactType
)

enum class ContactType {
    PRIVATE, WORK
}

object ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
        oldItem.countryCode == newItem.countryCode &&
                oldItem.phoneNum == newItem.phoneNum

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
        oldItem == newItem
}

fun Contact.getRelativeTimeSpan(): String =
    DateUtils.getRelativeTimeSpanString(
        this.accessDate.time,
        Date().time,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()