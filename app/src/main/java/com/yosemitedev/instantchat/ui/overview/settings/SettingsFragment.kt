package com.yosemitedev.instantchat.ui.overview.settings

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentSettingsBinding
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.themeColor
import com.yosemitedev.instantchat.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var settingsViewModel: SettingsViewModel

    private var binding by AutoClearedValue<FragmentSettingsBinding>(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel = viewModelProvider(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = settingsViewModel
            // Setup background drawable
            val backgroundContext = settingsContainer.context
            settingsContainer.background = MaterialShapeDrawable(
                backgroundContext,
                null,
                R.attr.bottomSheetStyle,
                0
            ).apply {
                fillColor = ColorStateList.valueOf(
                    backgroundContext.themeColor(R.attr.colorSurface)
                )
                elevation = resources.getDimension(R.dimen.plane_4)
                initializeElevationOverlay(requireContext())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            settingsThemesTextView.setOnClickListener {
                ThemeDialogFragment.newInstance().show(childFragmentManager, null)
            }
            settingsClientTextView.setOnClickListener {
                ClientDialogFragment.newInstance().show(childFragmentManager, null)
            }
            settingsDeleteContactsTextView.setOnClickListener {
                // Create confirm dialog for removing all records
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.remove_contact_title)
                    .setMessage(R.string.settings_delete_contacts_message)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        settingsViewModel.deleteAllContacts()
                    }
                    .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            settingsShowKeyboardSwitch.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setShowKeyboard(isChecked)
            }
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}