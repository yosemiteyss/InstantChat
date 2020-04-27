package com.yosemitedev.instantchat.ui.overview

import android.content.Intent
import androidx.lifecycle.*
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.model.ContactType.*
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.ui.AvatarStore
import com.yosemitedev.instantchat.utils.PreferencesHelper
import com.yosemitedev.instantchat.utils.SingleLiveEvent
import com.yosemitedev.instantchat.utils.WAClient
import com.yosemitedev.instantchat.utils.WAClientHelper
import com.yosemitedev.instantchat.utils.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class OverviewViewModel @Inject constructor(
    private val repository: ContactRepository,
    private val clientHelper: WAClientHelper,
    private val prefsHelper: PreferencesHelper
) : ViewModel() {

    private val _countryCode = MutableLiveData<String>()

    private val _phoneNum = MutableLiveData<String>()

    private val _contactType = MutableLiveData<ContactType>()

    private val _navigateToClient = SingleLiveEvent<Intent>()
    val navigateToClient: LiveData<Intent>
        get() = _navigateToClient

    val showInputContainer: LiveData<Boolean> =
        _countryCode.combine(_phoneNum).map { inputs ->
            inputs.first?.isNotEmpty() == true &&
            inputs.second?.isNotEmpty() == true
        }

    private val _showKeyboard =  SingleLiveEvent<Boolean>().apply {
        value = prefsHelper.showKeyboard
    }
    val showKeyboard: LiveData<Boolean>
        get() = _showKeyboard

    val privateContacts: LiveData<List<Contact>> =
        repository.getContacts(PRIVATE)

    val workContacts: LiveData<List<Contact>> =
        repository.getContacts(WORK)


    fun onCountryCodeChanged(countryCode: String) {
        _countryCode.value = countryCode
    }

    fun onPhoneNumChanged(phoneNum: String) {
        _phoneNum.value = phoneNum
    }

    fun onContactTypeChecked(contactType: ContactType) {
        _contactType.value = contactType
    }

    fun onChatButtonClicked() {
        addContact()
        _navigateToClient.value = clientHelper.getIntent(
            client = WAClient.values().first { it.packageName == prefsHelper.clientPackageName },
            countryCode = _countryCode.value!!,
            phoneNum = _phoneNum.value!!
        )
    }

    fun onContactItemClicked(contact: Contact) {
        updateContactAccessDate(contact)
        _navigateToClient.value = clientHelper.getIntent(
            client = WAClient.values().first { it.packageName == prefsHelper.clientPackageName },
            countryCode = contact.countryCode,
            phoneNum = contact.phoneNum
        )
    }

    private fun addContact() {
        viewModelScope.launch {
            val newContact = Contact(
                countryCode = _countryCode.value!!,
                phoneNum = _phoneNum.value!!,
                accessDate = Date(),
                avatar = AvatarStore.getAvatar(seed = _countryCode.value!! + _phoneNum.value!!),
                type = _contactType.value!!
            )
            repository.insertContact(newContact)
        }
    }

    private fun updateContactAccessDate(contact: Contact) {
        viewModelScope.launch {
            repository.updateAccessDate(
                countryCode = contact.countryCode,
                phoneNum = contact.phoneNum,
                date = Date()
            )
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact = contact)
        }
    }
}