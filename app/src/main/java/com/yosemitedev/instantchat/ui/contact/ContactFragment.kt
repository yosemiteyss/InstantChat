package com.yosemitedev.instantchat.ui.contact

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.FragmentContactBinding
import com.yosemitedev.instantchat.model.Category
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.ui.home.HomeFragmentDirections
import com.yosemitedev.instantchat.ui.home.HomeViewModel
import com.yosemitedev.instantchat.utils.AutoClearedValue
import com.yosemitedev.instantchat.utils.bindGoneIf
import com.yosemitedev.instantchat.utils.parentViewModels
import com.yosemitedev.instantchat.utils.themeColor
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class ContactFragment : Fragment(), ContactAdapter.ContactAdapterListener {

    private var binding: FragmentContactBinding by AutoClearedValue(this)

    private val viewModel: HomeViewModel by parentViewModels()

    private val category: Category by lazy {
        requireArguments().getSerializable(ARG_CATEGORY) as Category
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactBinding.inflate(inflater, container, false)

        // Setup recycler view
        val contactAdapter = ContactAdapter(this)

        binding.contactRecyclerView.apply {
            adapter = contactAdapter
            setHasFixedSize(true)
            buildItemTouchHelper(contactAdapter).attachToRecyclerView(this)
        }

        // Register data observers
        viewModel.getContacts(category).observe(viewLifecycleOwner) {
            contactAdapter.submitList(it)
            binding.noContactLayout.root.bindGoneIf(it.isNotEmpty())
        }

        return binding.root
    }

    override fun onContactClicked(contact: Contact) {
        viewModel.startChat(contact)
    }

    override fun onContactLongClicked(view: View, contact: Contact): Boolean {
        // To be implemented
        return true
    }

    private fun buildItemTouchHelper(adapter: ContactAdapter): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    // Remove contact
                    ItemTouchHelper.LEFT -> {
                        val contact = adapter.getContactItem(viewHolder.adapterPosition)
                        viewModel.deleteContact(contact)
                    }
                    // Edit avatar
                    ItemTouchHelper.RIGHT -> {
                        val contact = adapter.getContactItem(viewHolder.adapterPosition)
                        navigateToAvatarBottomSheet(contact)
                    }
                }

                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftBackgroundColor(requireContext().themeColor(R.attr.colorError))
                    .setSwipeLeftActionIconTint(requireContext().themeColor(R.attr.colorOnError))
                    .addSwipeRightActionIcon(R.drawable.ic_edit)
                    .addSwipeRightBackgroundColor(requireContext().themeColor(R.attr.colorSecondary))
                    .setSwipeRightActionIconTint(requireContext().themeColor(R.attr.colorOnSecondary))
                    .create()
                    .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        return ItemTouchHelper(callback)
    }


    private fun navigateToAvatarBottomSheet(contact: Contact) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAvatarBottomSheet(contact)
        )
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: Category): ContactFragment {
            return ContactFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CATEGORY, category)
                }
            }
        }
    }
}