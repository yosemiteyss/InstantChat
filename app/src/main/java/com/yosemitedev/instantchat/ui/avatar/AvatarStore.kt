package com.yosemitedev.instantchat.ui.avatar

import androidx.annotation.DrawableRes
import com.yosemitedev.instantchat.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AvatarStore @Inject constructor() {

    val avatars = listOf(
        R.drawable.avatar_boy,
        R.drawable.avatar_boy_1,
        R.drawable.avatar_girl,
        R.drawable.avatar_girl_1,
        R.drawable.avatar_man,
        R.drawable.avatar_man_1,
        R.drawable.avatar_man_2,
        R.drawable.avatar_man_3,
        R.drawable.avatar_man_4
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