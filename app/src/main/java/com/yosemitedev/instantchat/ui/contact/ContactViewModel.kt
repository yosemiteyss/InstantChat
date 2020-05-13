package com.yosemitedev.instantchat.ui.contact

import android.content.Intent
import androidx.lifecycle.*
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.model.ContactType.PRIVATE
import com.yosemitedev.instantchat.model.ContactType.WORK
import com.yosemitedev.instantchat.repository.ContactRepository
import com.yosemitedev.instantchat.repository.SettingsRepository
import com.yosemitedev.instantchat.utils.SingleLiveEvent
import com.yosemitedev.instantchat.utils.combine
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _countryCodeInput = MutableLiveData<String>()

    private val _phoneNumberInput = MutableLiveData<String>()

    private val _contactTypeInput = MutableLiveData<ContactType>()

    private val _launchClient = SingleLiveEvent<Intent>()
    val launchClient: LiveData<Intent>
        get() = _launchClient

    val showInputView: LiveData<Boolean> =
        _countryCodeInput
            .combine(_phoneNumberInput)
            .map {
                it.first?.isNotEmpty() == true && it.second?.isNotEmpty() == true
            }

    val showKeyboard: LiveData<Boolean> =
        liveData {
            emit(settingsRepository.getShowKeyboard())
        }

    val privateContacts: LiveData<List<Contact>> =
        contactRepository.getContacts(PRIVATE)

    val workContacts: LiveData<List<Contact>> =
        contactRepository.getContacts(WORK)


    fun setCountryCodeInput(countryCode: String) {
        _countryCodeInput.value = countryCode
    }

    fun setPhoneNumberInput(phoneNumber: String) {
        _phoneNumberInput.value = phoneNumber
    }

    fun setContactTypeInput(contactType: ContactType) {
        _contactTypeInput.value = contactType
    }

    fun startChat(contact: Contact? = null) {
        viewModelScope.launch {

            // When clicking contact item
            if (contact != null) {
                contactRepository.upsertContact(
                    contact.copy(accessDate = Date())
                )

                _launchClient.value = settingsRepository.getWAClientIntent(
                    countryCode = contact.countryCode,
                    phoneNumber = contact.phoneNum
                )
            }
            // When clicking chat button
            else {
                val countryCode = _countryCodeInput.value!!
                val phoneNumber = _phoneNumberInput.value!!
                val contactType = _contactTypeInput.value!!

                contactRepository.upsertContact(
                    countryCode = countryCode,
                    phoneNumber = phoneNumber,
                    contactType = contactType
                )

                _launchClient.value = settingsRepository.getWAClientIntent(
                    countryCode = countryCode,
                    phoneNumber = phoneNumber
                )
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact = contact)
        }
    }
}