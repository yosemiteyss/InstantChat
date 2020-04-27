package com.yosemitedev.instantchat.ui.overview.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.utils.Theme
import com.yosemitedev.instantchat.utils.parentViewModelProvider
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class ThemeDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var settingsViewModel: SettingsViewModel

    private val themeAdapter: ArrayAdapter<ThemeItemHolder> by lazy {
        ArrayAdapter<ThemeItemHolder>(requireContext(), R.layout.dialog_list_simple_choice)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create an alert dialog containing all themes
        return MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.settings_theme_title))
            .setSingleChoiceItems(themeAdapter, 0) { dialog, position ->
                dialog.dismiss()
                val theme = themeAdapter.getItem(position)!!.theme
                settingsViewModel.setTheme(theme)
            }
            .create()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel = parentViewModelProvider(viewModelFactory)
        settingsViewModel.apply {
            themes.observe(this@ThemeDialogFragment) { themes ->
                themeAdapter.clear()
                themeAdapter.addAll(themes.map { theme -> ThemeItemHolder(theme, getTitleForTheme(theme)) })
            }
            currentTheme.observe(this@ThemeDialogFragment, ::updateSelectedItem)
        }
    }

    private fun updateSelectedItem(selected: Theme?) {
        val selectedPosition = (0 until themeAdapter.count).indexOfFirst { index ->
            themeAdapter.getItem(index)?.theme == selected
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedPosition, true)
    }

    private fun getTitleForTheme(theme: Theme): String {
        return when (theme) {
            Theme.LIGHT -> getString(R.string.theme_light_title)
            Theme.DARK -> getString(R.string.theme_dark_title)
            Theme.SYSTEM -> getString(R.string.theme_system_title)
            Theme.BATTERY_SAVER -> getString(R.string.theme_battery_saver_title)
        }
    }

    private data class ThemeItemHolder(val theme: Theme, val title: String) {
        override fun toString(): String = title
    }

    companion object {
        fun newInstance() = ThemeDialogFragment()
    }
}