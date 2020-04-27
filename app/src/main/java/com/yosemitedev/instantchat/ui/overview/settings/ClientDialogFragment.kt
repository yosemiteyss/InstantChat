package com.yosemitedev.instantchat.ui.overview.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosemitedev.instantchat.R
import com.yosemitedev.instantchat.utils.WAClient
import com.yosemitedev.instantchat.utils.parentViewModelProvider
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class ClientDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var settingsViewModel: SettingsViewModel

    private val clientAdapter: ArrayAdapter<ClientItemHolder> by lazy {
        ArrayAdapter<ClientItemHolder>(requireContext(), R.layout.dialog_list_simple_choice)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(context)
            .setTitle(getString(R.string.settings_default_client_title))
            .setSingleChoiceItems(clientAdapter, 0) { dialog, position ->
                dialog.dismiss()
                val client = clientAdapter.getItem(position)!!.client
                settingsViewModel.setClient(client)
            }
            .create()
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settingsViewModel = parentViewModelProvider(viewModelFactory)
        settingsViewModel.apply {
            clients.observe(this@ClientDialogFragment) { clients ->
                clientAdapter.clear()
                clientAdapter.addAll(clients.map { client ->
                    ClientItemHolder(client, getTitleForClient(client))
                })
            }
            currentClient.observe(this@ClientDialogFragment, ::updateSelectedItem)
        }
    }

    private fun updateSelectedItem(selected: WAClient?) {
        val selectedPosition = (0 until clientAdapter.count).indexOfFirst { index ->
            clientAdapter.getItem(index)?.client == selected
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedPosition, true)
    }

    private fun getTitleForClient(client: WAClient): String {
        return when (client) {
            WAClient.DEFAULT -> getString(R.string.waclient_default_title)
            WAClient.BUSINESS -> getString(R.string.waclient_business_title)
        }
    }

    private data class ClientItemHolder(val client: WAClient, val title: String) {
        override fun toString(): String = title
    }

    companion object {
        fun newInstance() = ClientDialogFragment()
    }
}