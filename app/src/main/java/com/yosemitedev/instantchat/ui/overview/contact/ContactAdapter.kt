package com.yosemitedev.instantchat.ui.overview.contact

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.ItemContactBinding
import com.yosemitedev.instantchat.databinding.ItemContactHeaderBinding
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.ui.overview.contact.ContactViewHolder.*

class ContactAdapter(
    private val listener: ContactAdapterListener,
    headerTitle: String
) : RecyclerView.Adapter<ContactViewHolder>() {

    var contacts: List<Contact> = emptyList()
        set(value) {
            field = value
            differ.submitList(buildMergedList(contactItems = value))
        }

    private val headerItem = ContactHeaderItem(headerTitle)

    private val differ = AsyncListDiffer(this,
        DiffCallback
    )

    interface ContactAdapterListener {
        fun onContactClicked(contact: Contact)
        fun onContactLongClicked(view: View, contact: Contact): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_contact_header -> ContactHeaderViewHolder(
                ItemContactHeaderBinding.inflate(inflater, parent, false)
            )
            R.layout.item_contact -> ContactItemViewHolder(
                ItemContactBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        when (holder) {
            is ContactHeaderViewHolder -> holder.binding.apply {
                header = differ.currentList[position] as ContactHeaderItem
                executePendingBindings()
            }
            is ContactItemViewHolder -> holder.binding.apply {
                contact = differ.currentList[position] as Contact
                contactListener = listener
                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ContactHeaderItem -> R.layout.item_contact_header
            is Contact -> R.layout.item_contact
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    private fun buildMergedList(
        contactHeaderItem: ContactHeaderItem = headerItem,
        contactItems: List<Contact> = contacts
    ) : List<Any> {
        val mergedList = mutableListOf<Any>(contactHeaderItem)
        mergedList.addAll(contactItems)
        return mergedList
    }
}

data class ContactHeaderItem(val title: String)

object DiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Contact && newItem is Contact ->
                oldItem.countryCode == newItem.countryCode &&
                oldItem.phoneNum == newItem.phoneNum
            else -> true
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Contact && newItem is Contact -> oldItem == newItem
            else -> true
        }
    }
}

sealed class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class ContactHeaderViewHolder(
        val binding: ItemContactHeaderBinding
    ) : ContactViewHolder(binding.root)

    class ContactItemViewHolder(
        val binding: ItemContactBinding
    ) : ContactViewHolder(binding.root)
}