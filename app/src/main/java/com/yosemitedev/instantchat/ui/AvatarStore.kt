package com.yosemitedev.instantchat.ui

import androidx.annotation.DrawableRes
import com.yosemitedev.instantchat.R
import javax.inject.Inject
import javax.inject.Singleton

object AvatarStore {

    private val avatars = listOf(
        R.drawable.avatar_1_raster,
        R.drawable.avatar_2_raster,
        R.drawable.avatar_3_raster,
        R.drawable.avatar_4_raster,
        R.drawable.avatar_5_raster,
        R.drawable.avatar_6_raster
    )

    @DrawableRes
    fun getAvatar(seed: String): Int {
        return avatars[seed.hashCode() and 0xfffffff % avatars.size]
    }

    @DrawableRes
    fun getRandomAvatar(): Int {
        return avatars.random()
    }
}