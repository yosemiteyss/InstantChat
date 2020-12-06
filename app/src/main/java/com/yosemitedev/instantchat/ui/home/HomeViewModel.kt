package com.yosemitedev.instantchat.ui.home

import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.managers.PreferencesManager
import com.yosemitedev.instantchat.managers.WAClient
import com.yosemitedev.instantchat.managers.WAClientManager
import com.yosemitedev.instantchat.model.Category
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.ui.avatar.AvatarStore
import com.yosemitedev.instantchat.utils.SingleLiveEvent
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel @ViewModelInject constructor(
    private val contactRepository: ContactRepository,
    private val waClientManager: WAClientManager,
    private val preferencesManager: PreferencesManager,
    private val avatarStore: AvatarStore
) : ViewModel() {

    private val _countryCodeInputChannel = ConflatedBroadcastChannel<String>()
    private val _phoneNumInputChannel = ConflatedBroadcastChannel<String>()
    private val _categoryInputChannel = ConflatedBroadcastChannel<Category>()

    private val _launchClient = SingleLiveEvent<Intent>()
    val launchClient: LiveData<Intent>
        get() = _launchClient

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    val showExtraInputFields: LiveData<Boolean> =
        _countryCodeInputChannel
            .asFlow()
            .combine(_phoneNumInputChannel.asFlow()) { countryCode, phoneNum ->
                countryCode.isNotBlank() && phoneNum.isNotBlank()
            }.asLiveData(viewModelScope.coroutineContext)

    fun setCountryCodeInput(countryCode: String) {
        _countryCodeInputChannel.offer(countryCode)
    }

    fun setPhoneNumInput(phoneNum: String) {
        _phoneNumInputChannel.offer(phoneNum)
    }

    fun setCategoryInput(category: Category) {
        _categoryInputChannel.offer(category)
    }

    fun startChat() {
        viewModelScope.launch {
            val contact = Contact(
                countryCode = _countryCodeInputChannel.value,
                phoneNum = _phoneNumInputChannel.value,
                accessDate = Date(),
                category = _categoryInputChannel.value,
                avatarRes = avatarStore.getRandomAvatar()
            )

            contactRepository.updateOrInsertContact(contact)
            startChatActivity(contact)
        }
    }

    fun startChat(contact: Contact) {
        viewModelScope.launch {
            updateContactAccessDate(contact)
            startChatActivity(contact)
        }
    }

    private suspend fun updateContactAccessDate(contact: Contact) {
        contactRepository.updateOrInsertContact(
            contact.copy(accessDate = Date())
        )
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact)
        }
    }

    fun getContacts(category: Category): LiveData<List<Contact>> {
        return contactRepository.getContacts(category)
    }

    private fun startChatActivity(contact: Contact) = viewModelScope.launch {
        val defaultClient = preferencesManager.getDefaultWAClient().first()
        val intent = waClientManager.getIntent(
            client = WAClient.values().first { it.packageName == defaultClient },
            countryCode = contact.countryCode,
            phoneNum = contact.phoneNum
        )

        _launchClient.value = intent
    }
}