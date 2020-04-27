package com.yosemitedev.instantchat.ui.overview

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.model.ContactType.*
import com.yosemitedev.instantchat.ui.overview.contact.ContactFragment
import com.yosemitedev.instantchat.ui.overview.settings.SettingsFragment

class OverviewPagerAdapter(
    context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pages = listOf(
        Page(context.getString(R.string.tab_private_title))
        { ContactFragment.newInstance(PRIVATE) },

        Page(context.getString(R.string.tab_work_title))
        { ContactFragment.newInstance(WORK) },

        Page(context.getString(R.string.tab_settings_title))
        { SettingsFragment.newInstance() }
    )

    override fun getItem(position: Int): Fragment =
        pages[position].fragment()

    override fun getCount(): Int =
        pages.size

    override fun getPageTitle(position: Int): CharSequence? =
        pages[position].title

    data class Page(val title: String, val fragment: () -> Fragment)
}






