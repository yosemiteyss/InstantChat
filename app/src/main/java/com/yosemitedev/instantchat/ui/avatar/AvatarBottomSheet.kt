package com.yosemitedev.instantchat.ui.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.BottomSheetAvatarBinding
import com.yosemitedev.instantchat.utils.AutoClearedValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AvatarBottomSheet : BottomSheetDialogFragment(), AvatarAdapter.ContactAvatarAdapterListener {

    private var binding: BottomSheetAvatarBinding by AutoClearedValue(this)

    private val viewModel: AvatarViewModel by viewModels()

    private val navArgs: AvatarBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.updateContactAvatar(
            contact = navArgs.contact,
            avatarRes = avatarRes
        )
    }
}