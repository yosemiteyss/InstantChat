package com.yosemitedev.instantchat.ui.overview.contact

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.shape.MaterialShapeDrawable
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentContactBinding
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.model.ContactType.*
import com.yosemitedev.instantchat.ui.overview.OverviewViewModel
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.parentViewModelProvider
import com.yosemitedev.instantchat.utils.themeColor
import dagger.android.support.DaggerFragment
import javax.inject.Inject

private const val ARG_CONTACT_TYPE = "arg_contact_type"

class ContactFragment : DaggerFragment(),
    ContactAdapter.ContactAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var overviewViewModel: OverviewViewModel

    private var binding by AutoClearedValue<FragmentContactBinding>(this)

    private val contactType: ContactType by lazy {
        requireArguments().getSerializable(ARG_CONTACT_TYPE) as ContactType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overviewViewModel = parentViewModelProvider(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        binding.apply {
            // Setup background drawable
            val backgroundContext = contactContainer.context
            contactContainer.background = MaterialShapeDrawable(
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

            // Setup recycler view
            val headerTitle = when (contactType) {
                PRIVATE -> getString(R.string.tab_private_title)
                WORK -> getString(R.string.tab_work_title)
            }
            val contactAdapter = ContactAdapter(
                listener = this@ContactFragment,
                headerTitle = headerTitle
            )
            contactRecyclerView.apply {
                adapter = contactAdapter
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }

            when (contactType) {
                PRIVATE -> overviewViewModel.privateContacts.observe(viewLifecycleOwner) {
                    contactAdapter.contacts = it
                    noContactLayout.noContactContainer.visibility =
                        if (it.isEmpty()) View.VISIBLE else View.GONE
                }
                WORK -> overviewViewModel.workContacts.observe(viewLifecycleOwner) {
                    contactAdapter.contacts = it
                    noContactLayout.noContactContainer.visibility =
                        if (it.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        return binding.root
    }

    override fun onContactClicked(contact: Contact) {
        overviewViewModel.onContactItemClicked(contact)
    }

    override fun onContactLongClicked(view: View, contact: Contact): Boolean {
        showContactPopupMenu(view, contact)
        return true
    }

    private fun showContactPopupMenu(view: View, contact: Contact) {
        PopupMenu(requireContext(), view).apply {
            menuInflater.inflate(R.menu.contact_menu, menu)
            gravity = Gravity.END
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.contact_edit -> {
                        // Todo: contact edit fragment
                    }
                    R.id.contact_remove -> overviewViewModel.deleteContact(contact)
                }
                true
            }
        }.show()
    }

    companion object {
        fun newInstance(contactType: ContactType) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT_TYPE, contactType)
                }
            }
    }
}