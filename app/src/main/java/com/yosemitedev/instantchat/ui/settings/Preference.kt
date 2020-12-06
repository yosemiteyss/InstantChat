package com.yosemitedev.instantchat.ui.settings

import androidx.annotation.DrawableRes

abstract class Preference(
    open val key: String,
    open val title: String,
    @DrawableRes open val drawableRes: Int
)

data class ActionPreference(
    override val key: String,
    override val title: String,
    @DrawableRes override val drawableRes: Int,
    val dialogMessage: String
) : Preference(key, title, drawableRes)

data class ListPreference(
    override val key: String,
    override val title: String,
    @DrawableRes override val drawableRes: Int,
    val items: List<ListPreferenceItem>
) : Preference(key, title, drawableRes) {

    fun getDefaultListItemTitle(): String {
        return items.first { it.default }.title
    }
}

data class ListPreferenceItem(
    val id: String,
    val title: String,
    val value: String,
    val default: Boolean = false
)
