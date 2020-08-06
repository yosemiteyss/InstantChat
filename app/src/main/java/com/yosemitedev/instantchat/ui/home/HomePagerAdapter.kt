package com.yosemitedev.instantchat.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.model.Category.PRIVATE
import com.yosemitedev.instantchat.model.Category.WORK
import com.yosemitedev.instantchat.ui.contact.ContactFragment
import com.yosemitedev.instantchat.ui.settings.SettingsFragment

class HomePagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    context: Context
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    val homeTabPages: List<HomeTabPage> = listOf(
        HomeTabPage(
            title = context.getString(R.string.home_tab_category_private_title),
            fragment = { ContactFragment.newInstance(PRIVATE) }
        ),
        HomeTabPage(
            title = context.getString(R.string.home_tab_category_work_title),
            fragment = { ContactFragment.newInstance(WORK) }
        ),
        HomeTabPage(
            title = context.getString(R.string.home_tab_settings_title),
            fragment = { SettingsFragment.newInstance() }
        )
    )

    override fun getItemCount(): Int = homeTabPages.size

    override fun createFragment(position: Int): Fragment {
        return homeTabPages[position].fragment()
    }

    data class HomeTabPage(val title: String, val fragment: () -> Fragment)
}






