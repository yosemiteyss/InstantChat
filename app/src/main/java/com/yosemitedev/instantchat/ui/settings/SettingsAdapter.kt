package com.yosemitedev.instantchat.ui.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.databinding.PreferenceActionItemBinding
import com.yosemitedev.instantchat.databinding.PreferenceListItemBinding
import com.yosemitedev.instantchat.ui.settings.SettingsListModel.ActionPreferenceModel
import com.yosemitedev.instantchat.ui.settings.SettingsListModel.ListPreferenceModel
import com.yosemitedev.instantchat.ui.settings.SettingsViewHolder.ActionPreferenceViewHolder
import com.yosemitedev.instantchat.ui.settings.SettingsViewHolder.ListPreferenceViewHolder

class SettingsAdapter(
    private val listener: SettingsAdapterListener
) : ListAdapter<SettingsListModel, SettingsViewHolder>(SettingsListModelDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.preference_action_item -> ActionPreferenceViewHolder(
                PreferenceActionItemBinding.inflate(inflater, parent, false)
            )

            R.layout.preference_list_item -> ListPreferenceViewHolder(
                PreferenceListItemBinding.inflate(inflater, parent, false)
            )

            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        when (holder) {
            is ActionPreferenceViewHolder -> holder.binding.run {
                val currentItem = getItem(position) as ActionPreferenceModel
                actionPreferenceModel = currentItem

                preferenceLayout.setOnClickListener {
                    // Show confirm dialog
                    MaterialAlertDialogBuilder(preferenceLayout.context)
                        .setTitle(currentItem.actionPreference.title)
                        .setMessage(currentItem.actionPreference.dialogMessage)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            listener.onActionPreferenceConfirmed(currentItem.actionPreference.key)
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }

                executePendingBindings()
            }

            is ListPreferenceViewHolder -> holder.binding.run {
                val currentItem = getItem(position) as ListPreferenceModel
                listPreferenceModel = currentItem

                preferenceLayout.setOnClickListener {
                    // Show radio buttons dialog
                    val listItems = currentItem.listPreference.items

                    MaterialAlertDialogBuilder(preferenceLayout.context)
                        .setTitle(currentItem.listPreference.title)
                        .setSingleChoiceItems(
                            listItems.map { it.title }.toTypedArray(),
                            currentItem.listPreference.items.indexOfFirst { it.default }
                        ) { dialog, which ->
                            listener.onListPreferenceUpdated(
                                key = currentItem.listPreference.key,
                                item = listItems[which]
                            )

                            dialog.dismiss()
                        }
                        .show()
                }

                executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ActionPreferenceModel -> R.layout.preference_action_item
            is ListPreferenceModel -> R.layout.preference_list_item
        }
    }

    interface SettingsAdapterListener {
        fun onActionPreferenceConfirmed(key: String)
        fun onListPreferenceUpdated(key: String, item: ListPreferenceItem)
    }
}

sealed class SettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    class ActionPreferenceViewHolder(
        val binding: PreferenceActionItemBinding
    ) : SettingsViewHolder(binding.root)

    class ListPreferenceViewHolder(
        val binding: PreferenceListItemBinding
    ) : SettingsViewHolder(binding.root)
}

sealed class SettingsListModel {
    data class ActionPreferenceModel(
        val actionPreference: ActionPreference
    ) : SettingsListModel()

    data class ListPreferenceModel(
        val listPreference: ListPreference
    ) : SettingsListModel()
}

object SettingsListModelDiff : DiffUtil.ItemCallback<SettingsListModel>() {
    override fun areItemsTheSame(oldItem: SettingsListModel, newItem: SettingsListModel): Boolean {
        return when {
            oldItem is ActionPreferenceModel && newItem is ActionPreferenceModel ->
                oldItem.actionPreference.key == newItem.actionPreference.key
            oldItem is ListPreferenceModel && newItem is ListPreferenceModel ->
                oldItem.listPreference.key == newItem.listPreference.key
            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: SettingsListModel,
        newItem: SettingsListModel
    ): Boolean {
        return when {
            oldItem is ActionPreferenceModel && newItem is ActionPreferenceModel ->
                oldItem.actionPreference == newItem.actionPreference
            oldItem is ListPreferenceModel && newItem is ListPreferenceModel ->
                oldItem.listPreference == newItem.listPreference
            else -> false
        }
    }
}
