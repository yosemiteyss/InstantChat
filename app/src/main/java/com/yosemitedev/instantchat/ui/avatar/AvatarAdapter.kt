package com.yosemitedev.instantchat.ui.avatar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.*
import com.yosemitedev.instantchat.databinding.ItemAvatarBinding
import com.yosemitedev.instantchat.model.Contact

class AvatarAdapter(
    private val listener: ContactAvatarAdapterListener
) : Adapter<AvatarAdapter.ContactAvatarViewHolder>() {

    var avatarItems: List<AvatarItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    var contact: Contact? = null
        set(value) {
            field = value
            selectedPosition = differ.currentList.indexOfFirst {
                it.drawableRes == value?.avatarRes
            }
        }

    private val differ = AsyncListDiffer(this,
        AvatarItemDiffCallback
    )

    private var selectedPosition: Int = NO_POSITION
        set(value) {
            // When a new avatar is chosen, notify the deselection of the previous item and
            // selection of the new item
            if (value != field) {
                if (field != NO_POSITION) {
                    differ.currentList[field].isSelected = false
                    notifyItemChanged(field)
                }

                if (value != NO_POSITION) {
                    with(differ.currentList[value]) {
                        isSelected = true
                        listener.onAvatarSelected(this.drawableRes)
                    }
                    notifyItemChanged(value)
                }

                field = value
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAvatarViewHolder {
        return ContactAvatarViewHolder(
            ItemAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactAvatarViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ContactAvatarViewHolder(
        private val binding: ItemAvatarBinding
    ) : ViewHolder(binding.root) {

        fun bind(avatarItem: AvatarItem, newPosition: Int) {
            binding.apply {
                this.avatarItem = avatarItem
                avatarItemImageView.setOnClickListener {
                    selectedPosition = newPosition
                }
                executePendingBindings()
            }
        }
    }

    interface ContactAvatarAdapterListener {
        fun onAvatarSelected(@DrawableRes avatarRes: Int)
    }
}

object AvatarItemDiffCallback : DiffUtil.ItemCallback<AvatarItem>() {

    override fun areItemsTheSame(oldItem: AvatarItem, newItem: AvatarItem): Boolean {
        return oldItem.drawableRes == newItem.drawableRes
    }

    override fun areContentsTheSame(oldItem: AvatarItem, newItem: AvatarItem): Boolean {
        return oldItem == newItem
    }
}

data class AvatarItem(
    @DrawableRes val drawableRes: Int,
    var isSelected: Boolean = false
)
