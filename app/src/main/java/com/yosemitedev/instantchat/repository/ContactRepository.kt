package com.yosemitedev.instantchat.repository

import com.yosemitedev.instantchat.db.ContactDao
import com.yosemitedev.instantchat.model.Category
import com.yosemitedev.instantchat.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {

    fun getContacts(category: Category) =
        contactDao.getContacts(category)

    suspend fun updateOrInsertContact(contact: Contact) = withContext(Dispatchers.IO) {
        contactDao.updateOrInsertContact(contact)
    }

    suspend fun deleteContact(contact: Contact) = withContext(Dispatchers.IO) {
        contactDao.deleteContact(contact)
    }

    suspend fun deleteAllContacts() = withContext(Dispatchers.IO) {
        contactDao.deleteAllContacts()
    }
}