package com.yosemitedev.instantchat.ui.overview

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentOverviewBinding
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OverviewFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var overviewViewModel: OverviewViewModel

    private var binding by AutoClearedValue<FragmentOverviewBinding>(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overviewViewModel = viewModelProvider(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = overviewViewModel

            // Setup view pager and tab layout
            overviewViewPager.apply {
                val pagerAdapter = OverviewPagerAdapter(requireContext(), childFragmentManager)
                adapter = pagerAdapter
                pageMargin = resources.getDimension(R.dimen.spacing_large).toInt()
            }
            overviewTabLayout.setupWithViewPager(overviewViewPager)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // Setup input edit texts
            overviewCountryCodeEditText.doOnTextChanged { text, _, _, _ ->
                overviewViewModel.onCountryCodeChanged(text.toString())
            }
            overviewPhoneNumberEditText.doOnTextChanged { text, _, _, _ ->
                overviewViewModel.onPhoneNumChanged(text.toString())
            }

            // Setup radio group
            overviewContactTypeRadioGroup.apply {
                setOnCheckedChangeListener { _, checkedId ->
                    when (checkedId) {
                        R.id.overview_contact_type_private_button ->
                            overviewViewModel.onContactTypeChecked(ContactType.PRIVATE)
                        R.id.overview_contact_type_work_button ->
                            overviewViewModel.onContactTypeChecked(ContactType.WORK)
                    }
                }
                check(R.id.overview_contact_type_private_button)
            }
        }

        overviewViewModel.apply {
            showInputContainer.observe(viewLifecycleOwner) { show ->
                binding.overviewInputContainer.visibility = if (show) View.VISIBLE else View.GONE
            }

            navigateToClient.observe(viewLifecycleOwner) { intent ->
                try { startActivity(intent) }
                catch (e: ActivityNotFoundException) {
                    Snackbar.make(
                        binding.overviewContainer,
                        R.string.waclient_activity_not_found_message,
                        Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

            showKeyboard.observe(viewLifecycleOwner) { show ->
                if (show)
                    binding.overviewCountryCodeEditText.requestFocus()
            }
        }
    }
}