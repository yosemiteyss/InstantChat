package com.yosemitedev.instantchat.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosemitedev.instantchat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreferenceFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        setupClientPreference()
        setupThemePreference()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable over-scroll ripple effecft
        listView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference?.key) {
            // Remove all contacts
            getString(R.string.pref_key_remove_all_contacts) -> {
                showDeleteContactsConfirmDialog()
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun showDeleteContactsConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.contact_remove_title)
            .setMessage(R.string.settings_remove_all_message)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                viewModel.deleteAllContacts()
            }
            .setNegativeButton(android.R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupClientPreference() {
        val clientListPreference = findPreference<ListPreference>(getString(R.string.pref_key_default_client))!!

        clientListPreference.apply {
            entries = resources.getStringArray(R.array.waclient_names)
            entryValues = viewModel.waClients.map { it.packageName }.toTypedArray()

            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setClient(newValue as String)
                true
            }
        }
    }

    private fun setupThemePreference() {
        val themeListPreference = findPreference<ListPreference>(getString(R.string.pref_key_default_theme))!!
        themeListPreference.apply {
            entries = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resources.getStringArray(R.array.theme_names_q)
            } else {
                resources.getStringArray(R.array.theme_names)
            }
            entryValues = viewModel.themes.map { it.key }.toTypedArray()

            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setTheme(newValue as String)
                true
            }
        }
    }
}