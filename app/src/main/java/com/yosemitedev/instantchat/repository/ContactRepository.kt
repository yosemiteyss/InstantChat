package com.yosemitedev.instantchat.repository

import androidx.lifecycle.LiveData
import com.yosemitedev.instantchat.db.ContactDao
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val contactDao: ContactDao) {

    fun getContacts(contactType: ContactType): LiveData<List<Contact>> =
        contactDao.getContacts(contactType)

    suspend fun insertContact(contact: Contact) = withContext(Dispatchers.IO) {
        contactDao.insertContact(contact)
    }

    suspend fun updateAccessDate(countryCode: String, phoneNum: String, date: Date) = withContext(Dispatchers.IO) {
        contactDao.updateAccessDate(countryCode, phoneNum, date)
    }

    suspend fun deleteContact(contact: Contact) = withContext(Dispatchers.IO) {
        contactDao.deleteContact(contact)
    }

    suspend fun deleteAllContacts() = withContext(Dispatchers.IO) {
        contactDao.deleteAllContacts()
    }
}