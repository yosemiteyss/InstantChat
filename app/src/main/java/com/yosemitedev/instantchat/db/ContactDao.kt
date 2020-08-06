package com.yosemitedev.instantchat.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yosemitedev.instantchat.model.Category
import com.yosemitedev.instantchat.model.Contact

@Dao
interface ContactDao {

    // Retrieve all contacts order by their last access dates
    @Query("SELECT * FROM Contact WHERE category = :category ORDER BY access_date DESC")
    fun getContacts(category: Category): LiveData<List<Contact>>

    // Insert a contact
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact): Long

    // Update a contact
    @Update
    suspend fun updateContact(contact: Contact)

    // Insert or Update a contact
    @Transaction
    suspend fun updateOrInsertContact(contact: Contact) {
        val id = insertContact(contact)
        if (id == -1L) updateContact(contact)
    }

    // Delete a contact
    @Delete
    suspend fun deleteContact(contact: Contact)

    // Delete all contacts
    @Query("DELETE FROM Contact")
    suspend fun deleteAllContacts()
}