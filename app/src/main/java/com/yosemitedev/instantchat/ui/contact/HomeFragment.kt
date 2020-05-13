package com.yosemitedev.instantchat.ui.contact

import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialFadeThrough
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentHomeBinding
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.snackBar
import com.yosemitedev.instantchat.utils.viewModelProvider
import javax.inject.Inject

class HomeFragment : Fragment() {

    private var binding by AutoClearedValue<FragmentHomeBinding>(this)

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = viewModelProvider(providerFactory)
        exitTransition = MaterialFadeThrough.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = contactViewModel
        }

        // Setup view pager
        val homePagerAdapter =
            HomePagerAdapter(
                requireContext(),
                childFragmentManager
            )

        binding.apply {
            homeViewPager.apply {
                adapter = homePagerAdapter
                pageMargin = resources.getDimension(R.dimen.spacing_large).toInt()
            }
            homeTabLayout.setupWithViewPager(homeViewPager)
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).contactComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup input fields
        binding.homeCountryCodeEditText.doOnTextChanged { text, start, count, after ->
            contactViewModel.setCountryCodeInput(text.toString())
        }

        binding.homePhoneNumberEditText.doOnTextChanged { text, start, count, after ->
            contactViewModel.setPhoneNumberInput(text.toString())
        }

        binding.homeContactTypeRadioGroup.apply {
            setOnCheckedChangeListener { _, checkedId ->
                val contactType = when (checkedId) {
                    R.id.home_contact_type_private_button -> ContactType.PRIVATE
                    R.id.home_contact_type_work_button -> ContactType.WORK
                    else -> throw IllegalArgumentException("Invalid contact type")
                }
                contactViewModel.setContactTypeInput(contactType)
            }
            check(R.id.home_contact_type_private_button)
        }

        // Setup chat button
        binding.homeChatButton.setOnClickListener {
            contactViewModel.startChat()
        }

        // Setup observers
        contactViewModel.launchClient.observe(viewLifecycleOwner, Observer {
            try {
                startActivity(it)
            } catch (e: ActivityNotFoundException) {
                snackBar(
                    binding.homeConstraintLayout,
                    getString(R.string.waclient_activity_not_found_message)
                )
            }
        })
    }
}