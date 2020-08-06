package com.yosemitedev.instantchat.ui.home

import android.content.ActivityNotFoundException
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentHomeBinding
import com.yosemitedev.instantchat.model.Category.PRIVATE
import com.yosemitedev.instantchat.model.Category.WORK
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.shortToast
import com.yosemitedev.instantchat.utils.themeColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding by AutoClearedValue(this)

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
        }

        // Setup bottom sheet shape
        val backgroundContext = binding.homeNavContainerLayout.context
        binding.homeNavContainerLayout.background = MaterialShapeDrawable(
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


        // Setup tab layout and view pager
        val homePagerAdapter = HomePagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = viewLifecycleOwner.lifecycle,
            context = requireContext()
        )

        binding.homeNavViewPager.adapter = homePagerAdapter

        TabLayoutMediator(binding.homeNavTabLayout, binding.homeNavViewPager) { tab, position ->
            tab.text = homePagerAdapter.homeTabPages[position].title
        }.attach()


        // Setup edit text fields
        binding.homeHeaderCountryCodeEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setCountryCodeInput(text.toString())
        }

        binding.homeHeaderPhoneNumberEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setPhoneNumInput(text.toString())
        }


        // Setup radio group (default: check private button)
        binding.homeHeaderCategoryRadioGroup.run {
            setOnCheckedChangeListener { _, checkedId ->
                val categoryInput = when (checkedId) {
                    R.id.home_header_category_private_radio_button -> PRIVATE
                    R.id.home_header_category_work_radio_button -> WORK
                    else -> throw IllegalStateException("Invalid checkId: $checkedId")
                }

                viewModel.setCategoryInput(categoryInput)
            }

            check(R.id.home_header_category_private_radio_button)
        }


        // Setup start chat button
        binding.homeHeaderStartChatButton.setOnClickListener {
            viewModel.startChat()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show toast messages
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            shortToast(it)
        }

        // Register intent observers
        viewModel.launchClient.observe(viewLifecycleOwner) {
            try {
                startActivity(it)
            } catch (e: ActivityNotFoundException) {
                shortToast(getString(R.string.waclient_activity_not_found_message))
            }
        }
    }
}