package com.yosemitedev.instantchat.ui.settings

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.yosemitedev.instantchat.InstantChat
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentSettingsBinding
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.themeColor

class SettingsFragment : Fragment() {

    lateinit var settingsComponent: SettingsComponent

    private var binding by AutoClearedValue<FragmentSettingsBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupBackgroundShape()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingsComponent = (requireActivity().application as InstantChat).appComponent
            .settingsComponent()
            .create()
    }

    private fun setupBackgroundShape() {
        val backgroundContext = binding.settingsLinearLayout.context
        binding.settingsLinearLayout.background = MaterialShapeDrawable(
            backgroundContext,
            null,
            R.attr.bottomSheetStyle,
            0
        ).apply {
            fillColor = ColorStateList.valueOf(
                backgroundContext.themeColor(R.attr.colorSurface)
            )
            elevation = resources.getDimension(R.dimen.elevation_xmedium)
            initializeElevationOverlay(requireContext())
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}