package com.yosemitedev.instantchat.repository

import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import com.yosemitedev.instantchat.persistence.ContactDao
import com.yosemitedev.instantchat.utils.AvatarStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao,
    private val avatarStore: AvatarStore
) {

    @DrawableRes
    fun getAvatarDrawables(): List<Int> {
        return avatarStore.avatars
    }

    fun getContacts(contactType: ContactType): LiveData<List<Contact>> {
        return contactDao.getContacts(contactType)
    }

    suspend fun upsertContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.upsertContact(contact)
        }
    }

    suspend fun upsertContact(
        countryCode: String,
        phoneNumber: String,
        contactType: ContactType
    ) {
        withContext(Dispatchers.IO) {
            contactDao.upsertContact(
                Contact(
                    countryCode = countryCode,
                    phoneNum = phoneNumber,
                    accessDate = Date(),
                    avatarRes = avatarStore.getAvatar(seed = countryCode + phoneNumber),
                    type = contactType
                )
            )
        }
    }

    suspend fun updateContact(
        oldContact: Contact,
        newContact: Contact
    ) {
        withContext(Dispatchers.IO) {
            contactDao.updateContact(oldContact, newContact)
        }
    }

    suspend fun deleteContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.deleteContact(contact)
        }
    }

    suspend fun deleteAllContacts() {
        withContext(Dispatchers.IO) {
            contactDao.deleteAllContacts()
        }
    }
}