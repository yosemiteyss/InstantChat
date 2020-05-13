package com.yosemitedev.instantchat.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yosemitedev.instantchat.databinding.ItemAvatarBinding
import com.yosemitedev.instantchat.model.Contact

class AvatarAdapter(
    private val listener: ContactAvatarAdapterListener
) : RecyclerView.Adapter<AvatarAdapter.ContactAvatarViewHolder>() {

    var avatarItems: List<AvatarItem> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    var contact: Contact? = null
        set(value) {
            field = value
            checkedPosition = differ.currentList.indexOfFirst { it.drawable == value?.avatarRes }
        }

    private val differ = AsyncListDiffer(
        this,
        AvatarItemDiffCallback
    )

    private var checkedPosition: Int = RecyclerView.NO_POSITION
        set(value) {
            if (value != field) {
                if (field != RecyclerView.NO_POSITION) {
                    differ.currentList[field].isChecked = false
                    notifyItemChanged(field)
                }

                if (value != RecyclerView.NO_POSITION) {
                    with(differ.currentList[value]) {
                        isChecked = true
                        listener.onAvatarChecked(this)
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
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatarItem: AvatarItem, newPos: Int) {
            binding.apply {
                this.avatarItem = avatarItem
                itemAvatarCardView.setOnClickListener {
                    checkedPosition = newPos
                }
                executePendingBindings()
            }
        }
    }

    interface ContactAvatarAdapterListener {
        fun onAvatarChecked(avatarItem: AvatarItem)
    }
}

object AvatarItemDiffCallback : DiffUtil.ItemCallback<AvatarItem>() {

    override fun areItemsTheSame(oldItem: AvatarItem, newItem: AvatarItem): Boolean {
        return oldItem.drawable == newItem.drawable
    }

    override fun areContentsTheSame(oldItem: AvatarItem, newItem: AvatarItem): Boolean {
        return oldItem == newItem
    }
}

data class AvatarItem(@DrawableRes val drawable: Int, var isChecked: Boolean = false)