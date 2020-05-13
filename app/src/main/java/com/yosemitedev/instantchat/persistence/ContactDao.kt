package com.yosemitedev.instantchat.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yosemitedev.instantchat.model.Contact
import com.yosemitedev.instantchat.model.ContactType

@Dao
interface ContactDao {

    @Query("SELECT * FROM Contact WHERE type = :contactType ORDER BY access_date DESC")
    fun getContacts(contactType: ContactType): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact): Long

    @Update
    suspend fun updateContact(contact: Contact)

    @Transaction
    suspend fun updateContact(oldContact: Contact, newContact: Contact) {
        deleteContact(oldContact)
        insertContact(newContact)
    }

    @Transaction
    suspend fun upsertContact(contact: Contact) {
        val insertRes = insertContact(contact)
        if (insertRes == -1L) {
            updateContact(contact)
        }
    }

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM Contact")
    suspend fun deleteAllContacts()
}