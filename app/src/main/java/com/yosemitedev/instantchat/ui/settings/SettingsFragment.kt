package com.yosemitedev.instantchat.ui.settings

import androidx.fragment.app.Fragment
import com.yosemitedev.instantchat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        fun newInstance() = SettingsFragment()
    }
}