package com.yosemitedev.instantchat.ui.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.BottomSheetAvatarBinding
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.utils.AutoClearedValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarBottomSheet : BottomSheetDialogFragment(), AvatarAdapter.ContactAvatarAdapterListener {

    private var binding: BottomSheetAvatarBinding by AutoClearedValue(this)
    private val viewModel: AvatarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAvatarBinding.inflate(inflater, container, false)

        // Setup recycler view
        val avatarAdapter = AvatarAdapter(this)

        binding.avatarRecyclerView.run {
            adapter = avatarAdapter
            addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.spacing_large))
            setHasFixedSize(true)
        }

        viewModel.avatars.observe(viewLifecycleOwner) { avatars ->
            avatarAdapter.avatarItems = avatars.map {
                AvatarItem(drawableRes = it)
            }
        }


        // Setup confirm button
        binding.avatarConfirmButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onAvatarSelected(avatarRes: Int) {
        val contact = requireArguments().getParcelable<Contact>(ARG_CONTACT) ?:
            throw IllegalArgumentException("No contact argument.")

        viewModel.updateContactAvatar(
            contact = contact,
            avatarRes = avatarRes
        )
    }

    companion object {
        const val TAG = "avatar_bottom_sheet"
        const val ARG_CONTACT = "arg_contact"

        @JvmStatic
        fun newInstance(contact: Contact): AvatarBottomSheet {
            return AvatarBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CONTACT, contact)
                }
            }
        }
    }
}