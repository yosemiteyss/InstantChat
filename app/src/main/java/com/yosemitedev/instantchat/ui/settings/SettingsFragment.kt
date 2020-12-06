package com.yosemitedev.instantchat.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yosemitedev.instantchat.databinding.FragmentSettingsBinding
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_THEME
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.DEFAULT_WA_CLIENT
import com.yosemitedev.instantchat.ui.settings.PreferenceKeys.REMOVE_CONTACTS
import com.yosemitedev.instantchat.utils.AutoClearedValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(), SettingsAdapter.SettingsAdapterListener {

    private var binding: FragmentSettingsBinding by AutoClearedValue(this)
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Setup settings list
        val settingsAdapter = SettingsAdapter(this)

        binding.recyclerView.run {
            adapter = settingsAdapter
            setHasFixedSize(true)
        }

        viewModel.settingsListModel.observe(viewLifecycleOwner) {
            settingsAdapter.submitList(it)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onActionPreferenceConfirmed(key: String) {
        when (key) {
            REMOVE_CONTACTS -> viewModel.deleteAllContacts()
        }
    }

    override fun onListPreferenceUpdated(key: String, item: ListPreferenceItem) {
        when (key) {
            DEFAULT_WA_CLIENT -> viewModel.updateDefaultClient(packageName = item.value)
            DEFAULT_THEME -> viewModel.updateDefaultTheme(themeKey = item.value)
        }
    }
}