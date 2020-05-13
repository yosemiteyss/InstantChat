package com.yosemitedev.instantchat.ui.contact

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.shape.MaterialShapeDrawable
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentContactBinding
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.model.ContactType.PRIVATE
import com.yosemitedev.instantchat.model.ContactType.WORK
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.parentViewModelProvider
import com.yosemitedev.instantchat.utils.themeColor
import javax.inject.Inject

class ContactFragment : Fragment(), ContactAdapter.ContactAdapterListener {

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    private lateinit var contactViewModel: ContactViewModel

    private var binding by AutoClearedValue<FragmentContactBinding>(this)

    private val contactType: ContactType by lazy {
        requireArguments().getSerializable(ARG_CONTACT_TYPE) as ContactType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactViewModel = parentViewModelProvider(providerFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        setupBackgroundShape()
        setupRecyclerView()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).contactComponent.inject(this)
    }

    private fun setupBackgroundShape() {
        val backgroundContext = binding.contactFrameLayout.context
        binding.contactFrameLayout.background = MaterialShapeDrawable(
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

    private fun setupRecyclerView() {
        val headerTitle = when (contactType) {
            PRIVATE -> getString(R.string.contact_type_private_title)
            WORK -> getString(R.string.contact_type_work_title)
        }
        val contactAdapter =
            ContactAdapter(
                this@ContactFragment,
                headerTitle
            )

        binding.contactRecyclerView.apply {
            adapter = contactAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }

        when (contactType) {
            PRIVATE -> contactViewModel.privateContacts.observe(viewLifecycleOwner, Observer {
                contactAdapter.contacts = it
                setupNoItemLayout(it.isEmpty())
            })

            WORK -> contactViewModel.workContacts.observe(viewLifecycleOwner, Observer {
                contactAdapter.contacts = it
                setupNoItemLayout(it.isEmpty())
            })
        }
    }

    private fun setupNoItemLayout(show: Boolean) {
        if (show) {
            binding.noContactLayout.root.visibility = View.VISIBLE
        } else {
            binding.noContactLayout.root.visibility = View.GONE
        }
    }

    private fun showContactPopupMenu(view: View, contact: Contact) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.contact_menu, menu)
            gravity = Gravity.END
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.contact_edit -> {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToContactEditFragment(contact)
                        )
                    }
                    R.id.contact_remove -> contactViewModel.deleteContact(contact)
                }
                true
            }
        }.show()
    }

    override fun onContactClicked(contact: Contact) {
        contactViewModel.startChat(contact)
    }

    override fun onContactLongClicked(view: View, contact: Contact): Boolean {
        showContactPopupMenu(view, contact)
        return true
    }

    companion object {
        private const val ARG_CONTACT_TYPE = "arg_contact_type"

        fun newInstance(contactType: ContactType) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT_TYPE, contactType)
                }
            }
    }
}