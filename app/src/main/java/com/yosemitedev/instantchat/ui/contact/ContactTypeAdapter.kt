package com.yosemitedev.instantchat.ui.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.yosemitedev.instantchat.model.ContactType
import kotlinx.android.synthetic.main.item_contact_type.view.*

class ContactTypeAdapter(
    context: Context,
    @LayoutRes private val layout: Int
) : ArrayAdapter<ContactType>(context, layout) {

    var contactTypesItems = mutableListOf<ContactType>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)

        val rowView: View? =
            convertView ?: inflater.inflate(layout, parent, false)

        rowView?.item_contact_type_text_view?.text = getItem(position)?.getTitle(parent.context)

        return rowView!!
    }

    override fun getCount(): Int = contactTypesItems.size

    override fun getItem(position: Int): ContactType? = contactTypesItems[position]

    override fun getFilter(): Filter {
        return contactTypeFilter
    }

    private val contactTypeFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as ContactType).getTitle(context)
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return if (constraint != null) {
                contactTypesItems.clear()
                FilterResults().apply {
                    values = contactTypesItems
                    count = contactTypesItems.size
                }
            } else {
                FilterResults()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList = results?.values as List<ContactType>?

            if (results != null && results.count > 0) {
                clear()
                filteredList?.forEach { add(it) }
                notifyDataSetChanged()
            }
        }
    }
}
