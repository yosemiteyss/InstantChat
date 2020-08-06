package com.yosemitedev.instantchat.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yosemitedev.instantchat.databinding.ItemContactBinding
import com.yosemitedev.instantchat.model.Contact

class ContactAdapter(
    private val listener: ContactAdapterListener
) : ListAdapter<Contact, ContactItemViewHolder>(ContactItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactItemViewHolder(
            ItemContactBinding.inflate(inflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getContactItem(position: Int): Contact {
        return getItem(position)
    }

    interface ContactAdapterListener {
        fun onContactClicked(contact: Contact)
        fun onContactLongClicked(view: View, contact: Contact): Boolean
    }
}

object ContactItemDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.countryCode == newItem.countryCode &&
                oldItem.phoneNum == newItem.phoneNum
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactItemViewHolder(
    private val binding: ItemContactBinding,
    private val listener: ContactAdapter.ContactAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact) {
        binding.run {
            this.contact = contact
            this.listener = this@ContactItemViewHolder.listener
            executePendingBindings()
        }
    }
}