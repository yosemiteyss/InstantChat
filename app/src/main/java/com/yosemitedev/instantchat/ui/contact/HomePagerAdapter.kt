package com.yosemitedev.instantchat.ui.contact

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.model.ContactType.PRIVATE
import com.yosemitedev.instantchat.model.ContactType.WORK
import com.yosemitedev.instantchat.ui.settings.SettingsFragment

class HomePagerAdapter(
    context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = mutableListOf(
        Page(
            context.getString(
                R.string.contact_type_private_title
            )
        ) {
            ContactFragment.newInstance(PRIVATE)
        },
        Page(
            context.getString(
                R.string.contact_type_work_title
            )
        ) {
            ContactFragment.newInstance(WORK)
        },
        Page(
            context.getString(
                R.string.tab_settings_title
            )
        ) {
            SettingsFragment.newInstance()
        }
    )

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position].fragment()

    override fun getPageTitle(position: Int): CharSequence? = fragmentList[position].title

    data class Page(val title: String, val fragment: () -> Fragment)
}






