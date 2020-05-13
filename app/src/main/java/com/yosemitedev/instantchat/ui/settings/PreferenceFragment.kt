package com.yosemitedev.instantchat.ui.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosemitedev.instantchat.R
import javax.inject.Inject

class PreferenceFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    private val settingsViewModel: SettingsViewModel by viewModels { providerFactory }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        setupClientPreference()
        setupThemePreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            getString(R.string.pref_key_remove_all) -> {
                // Show confirm dialog
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.contact_remove_title)
                    .setMessage(R.string.settings_remove_all_message)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        settingsViewModel.deleteAllContacts()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireParentFragment() as SettingsFragment).settingsComponent.inject(this)
    }

    private fun setupClientPreference() {
        val clientListPreference =
            findPreference<ListPreference>(getString(R.string.pref_key_client))!!
        clientListPreference.apply {
            entries = resources.getStringArray(R.array.waclient_names)
            entryValues = settingsViewModel.waClients.map { it.packageName }.toTypedArray()
            setOnPreferenceChangeListener { _, newValue ->
                settingsViewModel.setClient(newValue as String)
                true
            }
        }
    }

    private fun setupThemePreference() {
        val themeListPreference =
            findPreference<ListPreference>(getString(R.string.pref_key_theme))!!
        themeListPreference.apply {
            entries = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                resources.getStringArray(R.array.theme_names_q)
            else resources.getStringArray(R.array.theme_names)
            entryValues = settingsViewModel.themes.map { it.key }.toTypedArray()
            setOnPreferenceChangeListener { _, newValue ->
                settingsViewModel.setTheme(newValue as String)
                true
            }
        }
    }
}