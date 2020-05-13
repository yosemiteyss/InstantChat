package com.yosemitedev.instantchat.ui.contact

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.repository.ContactRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ContactEditViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {

    var contact: Contact? = null

    var contactNameInput: String? = null

    var countryCodeInput: String? = null

    var phoneNumberInput: String? = null

    var contactTypeInput: ContactType? = null

    @DrawableRes
    var avatarResInput: Int? = null

    @DrawableRes
    val avatarDrawables: List<Int> = contactRepository.getAvatarDrawables()

    val contactTypes: LiveData<List<ContactType>> = liveData {
        emit(enumValues<ContactType>().asList())
    }

    fun updateContactDetail() {
        viewModelScope.launch {
            contact?.let { it ->
                it.copy(
                    name = contactNameInput ?: it.name,
                    countryCode = countryCodeInput ?: it.countryCode,
                    phoneNum = phoneNumberInput ?: it.phoneNum,
                    accessDate = Date(),
                    type = contactTypeInput ?: it.type,
                    avatarRes = avatarResInput ?: it.avatarRes
                ).let { updatedContact ->
                    contactRepository.updateContact(
                        oldContact = it,
                        newContact = updatedContact
                    )
                }
            }
        }
    }
}