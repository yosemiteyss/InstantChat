package com.yosemitedev.instantchat.ui.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentContactEditBinding
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.isEmpty
import com.yosemitedev.instantchat.utils.viewModelProvider
import javax.inject.Inject

class ContactEditFragment : Fragment(), AvatarAdapter.ContactAvatarAdapterListener {

    private var binding by AutoClearedValue<FragmentContactEditBinding>(this)

    private val args: ContactEditFragmentArgs by navArgs()

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    private lateinit var contactEditViewModel: ContactEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactEditViewModel = viewModelProvider(providerFactory)
        contactEditViewModel.contact = args.contact
        enterTransition = MaterialFadeThrough.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactEditBinding.inflate(inflater, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = contactEditViewModel

            // Setup avatars
            val avatarAdapter = AvatarAdapter(
                this@ContactEditFragment
            ).apply {
                avatarItems = contactEditViewModel.avatarDrawables.map { AvatarItem(it) }
                contact = args.contact
            }

            contactEditAvatarRecyclerView.apply {
                adapter = avatarAdapter
                isNestedScrollingEnabled = false
                setHasFixedSize(true)
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).contactComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Setup app bar
            contactEditAppBar.setOnClickListener {
                findNavController().navigateUp()
            }

            // Setup fab
            contactEditFab.setOnClickListener {
                contactEditCountryCodeEditText.let {
                    if (it.isEmpty()) {
                        contactEditCountryCodeTextInputLayout.error = getString(
                            R.string.contact_edit_country_code_empty
                        )
                        return@setOnClickListener
                    }
                }

                contactEditPhoneNumberEditText.let {
                    if (it.isEmpty()) {
                        contactEditPhoneNumberTextInputLayout.error = getString(
                            R.string.contact_edit_phone_number_empty
                        )
                        return@setOnClickListener
                    }
                }

                contactEditViewModel.updateContactDetail()
                findNavController().navigateUp()
            }

            // Setup input views
            contactEditNameEditText.doOnTextChanged { text, _, _, _ ->
                contactEditViewModel.contactNameInput = text.toString()
            }

            contactEditCountryCodeEditText.doOnTextChanged { text, _, _, _ ->
                contactEditViewModel.countryCodeInput = text.toString()
                contactEditCountryCodeTextInputLayout.error = null
            }

            contactEditPhoneNumberEditText.doOnTextChanged { text, _, _, _ ->
                contactEditViewModel.phoneNumberInput = text.toString()
                contactEditPhoneNumberTextInputLayout.error = null
            }

            // Setup contact type menu
            val contactTypeAdapter = ContactTypeAdapter(
                requireContext(),
                R.layout.item_contact_type
            )

            binding.contactEditTypeTextView.apply {
                setAdapter(contactTypeAdapter)

                // Set default text
                val selectedType = args.contact.type
                setText(selectedType.getTitle(requireContext()), false)

                setOnItemClickListener { _, _, position, _ ->
                    (adapter.getItem(position) as ContactType).let {
                        contactEditViewModel.contactTypeInput = it
                    }
                }
            }

            contactEditViewModel.contactTypes.observe(viewLifecycleOwner, Observer {
                contactTypeAdapter.contactTypesItems = it.toMutableList()
            })
        }
    }

    override fun onAvatarChecked(avatarItem: AvatarItem) {
        contactEditViewModel.avatarResInput = avatarItem.drawable
    }
}